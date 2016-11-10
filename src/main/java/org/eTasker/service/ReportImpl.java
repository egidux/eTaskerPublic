package org.eTasker.service;

import org.eTasker.model.Report;
import org.eTasker.repository.ReportRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportImpl implements ReportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
	
	@Autowired
	private ReportRepository reportRepository;
	private boolean init = true;

	public synchronized void init() {
		if (init) {
			Report report = new Report();
			report.setShow_description(Boolean.TRUE);
			report.setShow_duration(Boolean.TRUE);
			report.setShow_finish(Boolean.TRUE);
			report.setShow_price(Boolean.TRUE);
			report.setShow_start(Boolean.TRUE);
			report.setShow_text(Boolean.TRUE);
			reportRepository.save(report);
			init = false;
		}
	}
	
	@Override
	public Report get() {
		init();
		Report report = reportRepository.getOne(1);
		if (report == null) {
			LOGGER.debug("Failed retrieve report");
			return null;
		}
		LOGGER.info("Retrieved report:" + JsonBuilder.build(report));
		return report;
	}

	@Override
	public Report update(Report report) {
		init();
		Report reportUpdate = get();
		if (reportUpdate == null) {
			LOGGER.debug("Failed update report: no report found");
			return null;
		}
		if (report.getShow_description() != null) {
			reportUpdate.setShow_description(report.getShow_description());
			LOGGER.info("Report updated show description=" + report.getShow_description());
		}
		if (report.getShow_duration() != null) {
			reportUpdate.setShow_duration(report.getShow_duration());
			LOGGER.info("Report updated show duration=" + report.getShow_duration());
		}
		if (report.getShow_finish() != null) {
			reportUpdate.setShow_finish(report.getShow_finish());
			LOGGER.info("Report updated show task finish time=" + report.getShow_finish());
		}
		if (report.getShow_price() != null) {
			reportUpdate.setShow_price(report.getShow_price());
			LOGGER.info("Report updated show price=" + report.getShow_price());
		}
		if (report.getShow_start() != null) {
			reportUpdate.setShow_start(report.getShow_start());
			LOGGER.info("Report updated show task start time=" + report.getShow_start());
		}
		if (report.getShow_text() != null) {
			reportUpdate.setShow_text(report.getShow_text());
			LOGGER.info("Report updated show report informative text=" + report.getShow_text());
		}
		if (report.getCompany_address() != null && !report.getCompany_address().isEmpty()) {
			reportUpdate.setCompany_address(report.getCompany_address());
			LOGGER.info("Report updated company adress=" + report.getCompany_address());
		}
		if (report.getCompany_code() != null && !report.getCompany_code().isEmpty()) {
			reportUpdate.setCompany_code(report.getCompany_code());
			LOGGER.info("Report updated company code=" + report.getCompany_code());
		}
		if (report.getCompany_name() != null && !report.getCompany_name().isEmpty()) {
			reportUpdate.setCompany_name(report.getCompany_name());
			LOGGER.info("Report updated company name=" + report.getCompany_name());
		}
		if (report.getCompany_phone() != null && !report.getCompany_phone().isEmpty()) {
			reportUpdate.setCompany_phone(report.getCompany_phone());
			LOGGER.info("Report updated company phone=" + report.getCompany_phone());
		}
		LOGGER.info("Report updated successfully");
		return reportRepository.save(reportUpdate);
	}
}
