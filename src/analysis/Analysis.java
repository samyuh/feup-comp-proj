package analysis;

import pt.up.fe.comp.jmm.report.Report;

import java.util.ArrayList;
import java.util.List;

public class Analysis {
    MySymbolTable symbolTable;
    List<Report> reports;

    public Analysis() {
        this.symbolTable = new MySymbolTable();
        this.reports = new ArrayList<>();
    }

    public List<Report> getReports() {
        return reports;
    }

    public MySymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void addReport(Report report) {
        reports.add(report);
    }
}
