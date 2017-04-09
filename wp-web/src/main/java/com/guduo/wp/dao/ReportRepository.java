package com.guduo.wp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guduo.wp.domain.Order;
import com.guduo.wp.domain.Report;


public interface ReportRepository extends JpaRepository<Report, Integer>{
	List<Report> findByReportType(int reportType);
}
