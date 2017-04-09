package com.guduo.wp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guduo.wp.dao.DocumentRepository;
import com.guduo.wp.dao.ReportRepository;
import com.guduo.wp.domain.Document;
import com.guduo.wp.domain.Report;

@Service
public class DocumentService {
	@Autowired
	private DocumentRepository docRepo;
	
	@Autowired
	private ReportRepository reportRepo;
	
	public List<Document> getDocumentListByDocType(int docType){
		return docRepo.findByDocType(docType);
	}
	
	public List<Report> getReportList(int reportType){
		return reportRepo.findByReportType(reportType);
	}
}
