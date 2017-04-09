package com.guduo.wp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guduo.wp.domain.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer>{
	List<Document> findByDocType(int docType); 
}
