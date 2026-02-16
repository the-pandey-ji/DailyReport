package com.nfl.dailyreport.dto;

import jakarta.servlet.http.HttpServletRequest;

public class UreaDTO {

    // ===== Meta =====
    public String reportDate;        // dd/MM/yyyy
    public String userId;
    public String ip;

    // ===== Production =====
    public String ureaProd;
    public String neemUreaProd;
    public String plainToGold;

    // ===== Consumption =====
    public String co2;
    public String steam;
    public String steamHrs;

    // ===== Losses =====
    public String rawMat;
    public String power;
    public String mechanical;
    public String electrical;
    public String instrumentation;
    public String process;
    public String shutdown;
    public String annualShutdown;
    public String others;

    // ===== Dispatch =====
    public String rail;
    public String road;

    // ===== Stocks =====
    public String ureaCl;
    public String neemDesp;

    // ===== Bags =====
    public String bagRec;
    public String bagCons;
    public String bagCl;

    // ===== Neem Oil =====
    public String neemOilRec;
    public String neemOilCons;
    public String neemOilCl;

    // ===== Neem Bags =====
    public String neemBagRec;
    public String neemBagCons;
    public String neemBagCl;

    // ------------------------------------------------------------------
    // Factory method – clean replacement of Hashtable + DOM
    // ------------------------------------------------------------------
    public static UreaDTO fromRequest(HttpServletRequest req) {

        UreaDTO d = new UreaDTO();

        d.reportDate = req.getParameter("reportdate");
        d.userId     = req.getUserPrincipal() != null
                        ? req.getUserPrincipal().getName()
                        : "SYSTEM";
        d.ip         = req.getRemoteAddr();

        d.ureaProd        = p(req, "ureaproduction");
        d.neemUreaProd    = p(req, "neemureaproduction");
        d.plainToGold     = p(req, "plainureatogoldurea");

        d.co2        = p(req, "co2consumption");
        d.steam      = p(req, "steamconsumption");
        d.steamHrs   = p(req, "steamhours");

        d.rawMat     = p(req, "rawmaterials");
        d.power      = p(req, "exportpower");
        d.mechanical = p(req, "mechanical");
        d.electrical = p(req, "electrical");
        d.instrumentation = p(req, "instrumentation");
        d.process    = p(req, "process1");
        d.shutdown   = p(req, "shutdown");
        d.annualShutdown = p(req, "annualshutdown");
        d.others     = p(req, "others");

        d.rail       = p(req, "rail");
        d.road       = p(req, "road");

        d.ureaCl     = p(req, "ureaclosingstock");
        d.neemDesp   = p(req, "neemureadespatch");

        d.bagRec     = p(req, "receipt");
        d.bagCons    = p(req, "consumption");
        d.bagCl      = p(req, "bagclosingstock");

        d.neemOilRec  = p(req, "neemoilreceipt");
        d.neemOilCons = p(req, "neemoilconsumption");
        d.neemOilCl   = p(req, "neemoilclosingstock");

        d.neemBagRec  = p(req, "neemreceipt");
        d.neemBagCons = p(req, "neemconsumption");
        d.neemBagCl   = p(req, "neembagclosingstock");

        return d;
    }

    // ------------------------------------------------------------------
    // Utility – avoid nulls (old CommonLib behavior)
    // ------------------------------------------------------------------
    private static String p(HttpServletRequest r, String name) {
        String v = r.getParameter(name);
        return (v == null || v.isBlank()) ? "0" : v.trim();
    }
}
