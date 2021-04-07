package analysis;

import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

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

    public void addReport(JmmNode node, String message) {
        Report report = new Report(ReportType.ERROR, Stage.SEMANTIC,
                Integer.parseInt(node.get("line")), Integer.parseInt(node.get("col")),message);
        reports.add(report);
    }
}
