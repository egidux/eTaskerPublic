package org.eTasker.service;

import org.eTasker.model.Report;

public interface ReportService {
	
	Report get();

    Report update(Report report);
}
