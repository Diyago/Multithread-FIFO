
////////////////////////////////////////////////////////////////////////////////
// Concurrent Hopscotch Hash Map
//
////////////////////////////////////////////////////////////////////////////////
//TERMS OF USAGE
//----------------------------------------------------------------------
//
//  Permission to use, copy, modify and distribute this software and
//  its documentation for any purpose is hereby granted without fee,
//  provided that due acknowledgements to the authors are provided and
//  this permission notice appears in all copies of the software.
//  The software is provided "as is". There is no warranty of any kind.
//
//Programmers:
//  Hila Goel
//  Tel-Aviv University
//  and
//  Maya Gershovitz
//  Tel-Aviv University
//  
//
//  Date: January, 2015.
////////////////////////////////////////////////////////////////////////////////
//
// This code was developed as part of "Workshop on Multicore Algorithms" 
// at Tel-Aviv university, under the guidance of Prof. Nir Shavit and Moshe Sulamy.
//
////////////////////////////////////////////////////////////////////////////////

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.deuce.Atomic;

// This is one of eight implementations we created for the workshop.
// In this implementation we used a transactional memory method.
// We used a third party package - Deuce to implement atomic actions.
// When executing an action that changes the data, 
// we initially try to run it as an atomic action,
// if this attempt fails, we run the action using the fine-grained locking method.

public class hopscotch {

	//defs
	final static int HOP_RANGE = 32;
	final static int ADD_RANGE = 256;
	final static int MAX_SEGMENTS = 1048576; // Including neighbourhood for last hash location
	int containCount = 0;

	volatile Bucket segments_arys[];		// The actual table
	int BUSY;

	/*Bucket is the table object.
	Each bucket contains a key and data pairing (as in a usual hashmap),
	and an "hop_info" variable, containing the information 
	regarding all the keys that were initially mapped to the same bucket.*/
	class Bucket {

		volatile AtomicLong _hop_info;
		volatile AtomicInteger _key;
		volatile AtomicInteger _data;

		// The backup locking system
		java.util.concurrent.locks.ReentrantLock _lock;

		//CTR - bucket
		Bucket() {
			_hop_info = new AtomicLong(0);
			_key = new AtomicInteger(-1);
			_data = new AtomicInteger(-1);
			_lock = new java.util.concurrent.locks.ReentrantLock();
		}
	}

	//CTR - hopscotch
	hopscotch() {
		int size = MAX_SEGMENTS+256;
		segments_arys = new Bucket[size];
		for(int i = 0; i < size; i++) {
			segments_arys[i] = new Bucket();
		}
		BUSY = -1;
	}

	//methods
	
	/* void trial()
	This is a method used for debugging purposes*/
	void trial() {
		Bucket temp;
		int count = 0;
		for(int i = 0; i < MAX_SEGMENTS+256; i++) {
			temp = segments_arys[i];
			if(temp._key.get() != -1) {
				count++;
			}
		}
		System.out.println("Items in Hash = " + count);
		System.out.println("--------------------");
	}
	
	/* int contains(int key)
	Key - the key we'd like to search for in the table
	Returns the index of the key in the table; -1 otherwise*/
	@Atomic
	int contains(int key) {
		int hash = ((key)&(MAX_SEGMENTS-1));
		Bucket start_bucket = segments_arys[hash];
		long mask = 1;
		
		for(int i = 0; i < HOP_RANGE; ++i, mask <<= 1) {
			if((mask & start_bucket._hop_info.get()) >= 1){
				Bucket check_bucket = segments_arys[hash+i];
				if(key == check_bucket._key.get()) {
					return hash+i;
				}
			}
		}
		return -1;
	}

	/*This method atomically clears the information
	from the bucket the given key was mapped to*/
	@Atomic
	int atomic_remove(int key) {
		int remove_bucket_index, rc, distance;
		int hash = ((key)&(MAX_SEGMENTS-1));
		Bucket start_bucket = segments_arys[hash];
		remove_bucket_index = contains(key);
		distance = remove_bucket_index - hash;
		if (remove_bucket_index > -1) {
			Bucket remove_bucket = segments_arys[remove_bucket_index];
			rc = remove_bucket._data.get();
			remove_bucket._key.set(-1);
			remove_bucket._data.set(-1);
			start_bucket._hop_info.set(start_bucket._hop_info.get() & ~(1<<distance));
			return rc;
		}
		return -1;
	}
	
	/*int remove(int key)
	The function tries to run atomic_remove; else uses locks
	Key - the key we'd like to remove from the table
	Returns the data paired with key, if the table contained the key,
	and NULL otherwise*/
	int remove(int key) {
		int hash = ((key)&(MAX_SEGMENTS-1));
		Bucket start_bucket = segments_arys[hash];
		int rc;
		try {
			rc = atomic_remove(key);
			return rc;
		} catch(Exception e) {
			//System.err.println("deleted key " + key + " at index " + remove_bucket_index);
			start_bucket._lock.lock();
			int remove_bucket_index = contains(key);
			segments_arys[remove_bucket_index]._lock.lock();
			rc = atomic_remove(key);
			start_bucket._lock.unlock();
			segments_arys[remove_bucket_index]._lock.unlock();
			return rc;
		}
	}

	/*int[] find_closer_bucket(int free_bucket_index, int free_distance, int val)
	free_bucket_index - the index of the first empty bucket in the table 
	(not in the neighbourhood)
	free_distance - the function return a value via this var
	val - the function return a value via this var
	
	Return an array of return values -
	0 - free distance, 1 - val, 2 - new free bucket
	"free_distance" the distance between start_bucket and the newly freed bucket
	Returns in val 0, if it was able to free a bucket in the neighbourhood of start_bucket,
	otherwise, val remains unchanged
	new_free_bucket the index of the newly freed bucket*/
	int[] find_closer_bucket(int free_bucket_index,int free_distance,int val) {
		//0 - free distance, 1 - val, 2 - new free bucket
		int[] result = new int[3];
		int move_bucket_index = free_bucket_index - (HOP_RANGE-1);
		Bucket move_bucket = segments_arys[move_bucket_index];
		for(int free_dist = (HOP_RANGE - 1); free_dist > 0; --free_dist) {
			long start_hop_info = move_bucket._hop_info.get();
			int move_free_distance = -1;
			long mask = 1;
			for (int i = 0; i < free_dist; ++i, mask <<= 1) {
				if((mask & start_hop_info) >= 1) {
					move_free_distance = i;
					break;
				}		
			}
			/*When a suitable bucket is found, it's content is moved to the old free_bucket*/
			if (-1 != move_free_distance) {
				if(start_hop_info == move_bucket._hop_info.get()) {
					int new_free_bucket_index = move_bucket_index + move_free_distance;
					Bucket new_free_bucket = segments_arys[new_free_bucket_index];
					/*Updates move_bucket's hop_info, to indicate the newly inserted bucket*/
					move_bucket._hop_info.set(move_bucket._hop_info.get() | (1 << free_dist));
					segments_arys[free_bucket_index]._data.set(new_free_bucket._data.get()) ;
					segments_arys[free_bucket_index]._key.set(new_free_bucket._key.get());
					new_free_bucket._key.set(BUSY);
					new_free_bucket._data.set(BUSY);
					/*Updates move_bucket's hop_info, to indicate the deleted bucket*/
					move_bucket._hop_info.set(move_bucket._hop_info.get() & ~(1<<move_free_distance));
					free_distance = free_distance - free_dist + move_free_distance;
					result[0] = free_distance;
					result[1] = val;
					result[2] = new_free_bucket_index;
					return result;
				}
			}
			++move_bucket_index;
			move_bucket = segments_arys[move_bucket_index];
		}
		segments_arys[free_bucket_index]._key.set(-1);
		result[0] = 0;
		result[1] = 0;
		result[2] = 0;
		return result;
	}

	/*This method atomically inserts the key and data pair
	to the suitable bucket in the table*/
	@Atomic
	void atomic_insert(Bucket start_bucket, Bucket free_bucket, int data, 
			int key, int free_distance) {
		if(contains(key) == -1) {
			start_bucket._hop_info.set(start_bucket._hop_info.get() | (1<<free_distance));
			free_bucket._data.set(data);
			free_bucket._key.set(key);
		}
	}
	
	/* boolean add(int key, int data)
	The function tries to run atomic_insert; else uses locks
	Key, Data - the key and data pair we'd like to add to the table.
	Returns true if the operation was successful, and false otherwise*/
	boolean add(int key, int data) {
		int val = 1;
		int hash = ((key)&(MAX_SEGMENTS-1));
		Bucket start_bucket = segments_arys[hash];
		
		int free_bucket_index = hash;
		Bucket free_bucket = segments_arys[hash];
		int free_distance = 0;
		for(; free_distance < ADD_RANGE; ++free_distance) {
			if(-1 == free_bucket._key.get() && true == free_bucket._key.compareAndSet(-1, BUSY))
				break;
			
			++free_bucket_index;
			free_bucket = segments_arys[free_bucket_index];
		}
		//0 - free distance, 1 - val, 2 - new_free_bucket_index
		int[] closest_bucket_info = new int[3];
		if(free_distance < ADD_RANGE) {
			do{
				if(free_distance < HOP_RANGE) {
					try {
						atomic_insert(start_bucket, free_bucket, data, key, free_distance);
					} catch(Exception e) {
						start_bucket._lock.lock();
						atomic_insert(start_bucket, free_bucket, data, key, free_distance);
						start_bucket._lock.unlock();
					}
					return true;
				} else {
					/*In case a free space was not found in the neighbourhood of start_bucket,
					Clears such a space*/
					closest_bucket_info = find_closer_bucket(free_bucket_index, free_distance, val);
					free_distance = closest_bucket_info[0];
					val = closest_bucket_info[1];
					free_bucket_index = closest_bucket_info[2];
					free_bucket = segments_arys[free_bucket_index];
				}
			} while(0 != val);
		}
		//System.out.println("Called Resize");
		return false;
	}
}
