package com.example.asg4.sqlite;
/*
 * Copyright (c) 2020 Ian Clement. All rights reserved.
 */

import java.util.List;

/**
 * A framework for CRUD operations.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public interface CRUDRepository<K, T> {

  /**
   * Create an element in the repository.
   *
   * @param element
   * @return the key indicating the element.
   * @throws DatabaseException
   */
  K create(T element) throws DatabaseException;

  /**
   * Read an element from the repository by its ID.
   *
   * @param key
   * @return the element from the repository.
   * @throws DatabaseException
   */
  T read(K key) throws DatabaseException;

  /**
   * Read all elements from the repository.
   *
   * @return the elements from the repository.
   * @throws DatabaseException
   */
  List<T> readAll() throws DatabaseException;

  /**
   * Update an element in the repository.
   *
   * @param element
   * @return true if element was in the repository, false otherwise.
   * @throws DatabaseException
   */
  boolean update(T element) throws DatabaseException;

  /**
   * Delete an element from the repository.
   *
   * @param element
   * @return true if the element was in the repository, false otherwise.
   * @throws DatabaseException
   */
  boolean delete(T element) throws DatabaseException;
}
