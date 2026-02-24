<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
Map<String,Object> r = (Map<String,Object>)request.getAttribute("report");
String selectedDate = (String)request.getAttribute("selectedDate");
if(selectedDate == null) selectedDate = "";
%>
<!DOCTYPE html>
<html>
<head>
<title>दैनिक रिपोर्ट DAILY REPORT</title>
<style>
    body { font-family: "Arial", sans-serif; font-size: 13px; margin: 20px; }
    .report-wrapper { max-width: 950px; margin: auto; }
    
    .header-block { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 2px; }
    .header-title { text-align: center; flex-grow: 1; font-size: 16px; font-weight: bold; }
    
    table { width: 100%; border-collapse: collapse; margin-bottom: -1px; table-layout: fixed; }
    th, td { border: 1px solid #000; padding: 3px 5px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    th { background: #e0e0e0; font-weight: bold; text-align: center; }
    
    .text-left { text-align: left; }
    .text-center { text-align: center; }
    .text-right { text-align: right; }
    .fw-bold { font-weight: bold; }
    .bg-light { background: #f0f0f0; }
    
    .status-box { border: 1px solid #000; padding: 8px; min-height: 100px; line-height: 1.5; }
    
    @media print { .no-print { display: none; } }
</style>
</head>
<body>

<div class="report-wrapper">

    <form method="get" action="pnpDailyReport" class="no-print" style="margin-bottom: 20px; text-align:center;">
        <b>Generate Report For Date :</b>
        <input type="date" name="reportDate" required value="<%=selectedDate%>">
        <button type="submit">Generate</button>
    </form>

<% if(r != null && !r.isEmpty()){ %>

    <div class="header-block">
        <div style="font-size:12px;"><b>IE-IF-02</b></div>
        <div class="header-title">दैनिक रिपोर्ट DAILY REPORT</div>
        <div></div>
    </div>
    <div class="header-block" style="border-bottom: 2px solid #000; padding-bottom: 5px; margin-bottom:0;">
        <div style="font-size:14px;">ने.फ.लि. पानीपत &nbsp;&nbsp;&nbsp; <b>NFL, PANIPAT</b></div>
        <div style="font-size:14px;">प्रातः 5 बजे को समाप्त दिनांक &nbsp;&nbsp;&nbsp; <b><%= selectedDate %></b></div>
    </div>

    <table>
        <colgroup>
            <col style="width:13%">
            <col style="width:12%">
            <col style="width:9%">
            <col style="width:10%">
            <col style="width:12%">
            <col style="width:14%">
            <col style="width:10%">
            <col style="width:9%">
            <col style="width:10%">
            <col style="width:11%">
        </colgroup>
        <tr>
            <th class="text-left" colspan="2">उत्पादन &nbsp; मी.टन &nbsp; Prodn</th>
            <th>दिन</th>
            <th>माह</th>
            <th>वर्ष</th>
            <th class="text-left" colspan="2">खपत &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; मी टन</th>
            <th>दिन</th>
            <th>माह</th>
            <th>वर्ष</th>
        </tr>
        <tr>
            <td class="text-left fw-bold" rowspan="2">अमोनिया</td>
            <td class="text-left">- लक्ष्य</td>
            <td class="text-right"></td>
            <td class="text-right">26724</td>
            <td class="text-right">330097</td>
            <td class="text-left bg-light">अमोनिया प्लांट</td>
            <td class="text-right bg-light"></td>
            <td class="text-right bg-light">24.00</td>
            <td class="text-right bg-light"></td>
            <td class="text-right bg-light"></td>
        </tr>
        <tr>
            <td class="text-left">- वास्तविक</td>
            <td class="text-right"><%= r.get("D_AMM_PROD") %></td>
            <td class="text-right"><%= r.get("M_AMM_PROD") %></td>
            <td class="text-right"><%= r.get("Y_AMM_PROD") %></td>
            <td class="text-left">फीड एन.जी.</td>
            <td class="text-right"></td>
            <td class="text-right"><%= r.get("D_AMM_NG_CONS") %></td>
            <td class="text-right"><%= r.get("M_AMM_NG_CONS") %></td>
            <td class="text-right"><%= r.get("Y_AMM_NG_CONS") %></td>
        </tr>
        <tr>
            <td class="text-left fw-bold" rowspan="2">यूरिया</td>
            <td class="text-left">- लक्ष्य</td>
            <td class="text-right"></td>
            <td class="text-right">44542</td>
            <td class="text-right">550000</td>
            <td class="text-left bg-light">स्टीम</td>
            <td class="text-right bg-light"></td>
            <td class="text-right bg-light">2104</td>
            <td class="text-right bg-light">46912</td>
            <td class="text-right bg-light">666774</td>
        </tr>
        <tr>
            <td class="text-left">- वास्तविक</td>
            <td class="text-right"><%= r.get("D_UREA_PROD") %></td>
            <td class="text-right"><%= r.get("M_UREA_PROD") %></td>
            <td class="text-right"><%= r.get("Y_UREA_PROD") %></td>
            <td class="text-left">यूरिया प्लांट</td>
            <td class="text-right"></td>
            <td class="text-right">24.00</td>
            <td class="text-right"></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold" rowspan="2">स्टीम</td>
            <td class="text-left">- एसजीपी</td>
            <td class="text-right"><%= r.get("D_STM_PROD_SGP") %></td>
            <td class="text-right"><%= r.get("M_STM_SGP") %></td>
            <td class="text-right"><%= r.get("Y_STM_SGP") %></td>
            <td class="text-left bg-light">अमोनिया</td>
            <td class="text-right bg-light"></td>
            <td class="text-right bg-light"><%= r.get("D_AMM_TO_UREA") %></td>
            <td class="text-right bg-light"><%= r.get("M_AMM_TO_UREA") %></td>
            <td class="text-right bg-light"><%= r.get("Y_AMM_TO_UREA") %></td>
        </tr>
        <tr>
            <td class="text-left">- एचआरएसजी</td>
            <td class="text-right"><%= r.get("D_STM_PROD_CPP") %></td>
            <td class="text-right"><%= r.get("M_STM_CPP") %></td>
            <td class="text-right"><%= r.get("Y_STM_CPP") %></td>
            <td class="text-left">सीओ2</td>
            <td class="text-right"></td>
            <td class="text-right"><%= r.get("D_UREA_CO2_CONS") %></td>
            <td class="text-right"><%= r.get("M_UREA_CO2") %></td>
            <td class="text-right"><%= r.get("Y_UREA_CO2") %></td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">बेंटोनाइट सल्फर</td>
            <td class="text-right"><%= r.get("D_BS_PROD") %></td>
            <td class="text-right"><%= r.get("M_BS_PROD") %></td>
            <td class="text-right"><%= r.get("Y_BS_PROD") %></td>
            <td class="text-left bg-light">स्टीम</td>
            <td class="text-right bg-light"></td>
            <td class="text-right bg-light"><%= r.get("D_UREA_STEAM_CONS") %></td>
            <td class="text-right bg-light"><%= r.get("M_UREA_STM") %></td>
            <td class="text-right bg-light"><%= r.get("Y_UREA_STM") %></td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">पावर उत्पादन (मेगावाट)</td>
            <td class="text-right"><%= r.get("D_GTGPOWERGEN") %></td>
            <td class="text-right"><%= r.get("M_PWR_GEN") %></td>
            <td class="text-right"><%= r.get("Y_PWR_GEN") %></td>
            <td class="text-left fw-bold">एसजीपी बायलर</td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">पावर आयात (मेगावाट)</td>
            <td class="text-right"><%= r.get("D_POWR_IMPORT") %></td>
            <td class="text-right"><%= r.get("M_PWR_IMP") %></td>
            <td class="text-right"><%= r.get("Y_PWR_IMP") %></td>
            <td class="text-left bg-light">कोयला</td>
            <td class="text-right bg-light"></td>
            <td class="text-right bg-light"><%= r.get("D_COAL_CONS_SGP") %></td>
            <td class="text-right bg-light"><%= r.get("M_COAL_SGP") %></td>
            <td class="text-right bg-light"><%= r.get("Y_COAL_SGP") %></td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">खपत एन.जी. MMSMQ</td>
            <td class="text-right"><%= r.get("D_NG_RECEIPT") %></td>
            <td class="text-right"><%= r.get("M_NG_REC") %></td>
            <td class="text-right"><%= r.get("Y_NG_REC") %></td>
            <td class="text-left">फ्यूल एन.जी.</td>
            <td class="text-right"></td>
            <td class="text-right"><%= r.get("D_FUEL_NG_CONS_SGP") %></td>
            <td class="text-right"><%= r.get("M_FNG_SGP") %></td>
            <td class="text-right"><%= r.get("Y_FNG_SGP") %></td>
        </tr>
        <tr>
            <td class="text-left fw-bold bg-light" colspan="2">प्रेषण मी.टन &nbsp; Desp</td>
            <td class="text-center bg-light">दिन</td>
            <td class="text-center bg-light">माह</td>
            <td class="text-center bg-light">वर्ष</td>
            <td class="text-left bg-light">स्टीम बीएसपी</td>
            <td class="text-right bg-light"></td>
            <td class="text-right bg-light">0.0000</td>
            <td class="text-right bg-light">0.0000</td>
            <td class="text-right bg-light">0.0000</td>
        </tr>
        <tr>
            <td class="text-left fw-bold" rowspan="3">यूरिया</td>
            <td class="text-left">रेल</td>
            <td class="text-right"><%= r.get("D_RAIL_DESP") %></td>
            <td class="text-right"><%= r.get("M_RAIL") %></td>
            <td class="text-right"><%= r.get("Y_RAIL") %></td>
            <td class="text-left">एचआरएसजी एनजी</td>
            <td class="text-right"></td>
            <td class="text-right"><%= r.get("D_NG_CONSM_HRSG") %></td>
            <td class="text-right"><%= r.get("M_NG_HRSG") %></td>
            <td class="text-right"><%= r.get("Y_NG_HRSG") %></td>
        </tr>
        <tr>
            <td class="text-left">सड़क</td>
            <td class="text-right"><%= r.get("D_ROAD_DESP") %></td>
            <td class="text-right"><%= r.get("M_ROAD") %></td>
            <td class="text-right"><%= r.get("Y_ROAD") %></td>
            <td class="text-left bg-light">जीटीजी एन.जी.</td>
            <td class="text-right bg-light"></td>
            <td class="text-right bg-light"><%= r.get("D_NG_CONSM_GTG") %></td>
            <td class="text-right bg-light"><%= r.get("M_NG_GTG") %></td>
            <td class="text-right bg-light"><%= r.get("Y_NG_GTG") %></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">कुल</td>
            <td class="text-right"><%= r.get("D_UREA_TOTAL_DESP") %></td>
            <td class="text-right"><%= r.get("M_UREA_TOTAL_DESP") %></td>
            <td class="text-right"><%= r.get("Y_UREA_TOTAL_DESP") %></td>
            <td class="text-left">टी.जी.1 स्टीम</td>
            <td class="text-right"></td>
            <td class="text-right"><%= r.get("D_STM_CONS_TG1_CPP") %></td>
            <td class="text-right">0</td>
            <td class="text-right">0</td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">अमो. प्रेषण</td>
            <td class="text-right"><%= r.get("D_AMM_TO_OTHER_UNITS") %></td>
            <td class="text-right"><%= r.get("M_AMM_DESP") %></td>
            <td class="text-right"><%= r.get("Y_AMM_DESP") %></td>
            <td class="text-left bg-light">टी.जी.2 स्टीम</td>
            <td class="text-right bg-light"></td>
            <td class="text-right bg-light"><%= r.get("D_STM_CONS_TG2_CPP") %></td>
            <td class="text-right bg-light">0</td>
            <td class="text-right bg-light">0</td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">बेंटोनाइट सल्फर</td>
            <td class="text-right"><%= r.get("D_BS_DESP") %></td>
            <td class="text-right"><%= r.get("M_BS_DESP") %></td>
            <td class="text-right"><%= r.get("Y_BS_DESP") %></td>
            <td class="text-left" colspan="5"></td>
        </tr>
        
        <tr>
            <th class="text-left" colspan="2">शेष स्टाक &nbsp;&nbsp; (मी.टन / नं.)</th>
            <th class="text-left" colspan="8">Closing Stock (MT/No.)</th>
        </tr>
        <tr>
            <td class="text-left fw-bold bg-light">अमोनिया</td>
            <td class="text-right bg-light" colspan="2"><%= r.get("D_AMM_CL_STK") %></td>
            <td class="text-left fw-bold bg-light" colspan="2">यूरिया &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= r.get("D_UREA_CL_STK") %></td>
            <td class="text-left fw-bold bg-light" colspan="2">कोयला &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= r.get("D_COAL_CL_STK") %></td>
            <td class="text-left fw-bold bg-light">थैले</td>
            <td class="text-right bg-light" colspan="2"><%= r.get("D_HDPE_BAG_CL_STK") %></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">एच2एसओ4</td>
            <td class="text-right" colspan="2"><%= r.get("D_H2SO4_CL_STK") %></td>
            <td class="text-left fw-bold" colspan="2">ए.एम.डी.ई.ए. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= r.get("D_AMDEA_CL_STK") %></td>
            <td class="text-left fw-bold" colspan="2">एनएओएच &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= r.get("D_NAOH_CL_STK") %></td>
            <td class="text-left fw-bold">नीम आयल</td>
            <td class="text-right" colspan="2"><%= r.get("D_NEEM_OIL_STK") %></td>
        </tr>
        <tr>
            <td class="text-left fw-bold bg-light">बेंटोनाइट सल्फर</td>
            <td class="text-right bg-light" colspan="2"><%= r.get("D_BS_CL_STK") %></td>
            <td class="bg-light" colspan="7"></td>
        </tr>

        <tr>
            <th class="text-left" colspan="4">विशिष्ट खपत Specific Consumption</th>
            <th class="text-center" colspan="2">निर्धारित लक्ष्य</th>
            <th class="text-center" colspan="2">दिन</th>
            <th class="text-center">माह</th>
            <th class="text-center">वर्ष</th>
        </tr>
        <tr class="bg-light">
            <td class="text-left fw-bold" colspan="2">फीड एन.जी./यूरिया</td>
            <td class="text-center" colspan="2">केएसएम3/मी.टन</td>
            <td class="text-center" colspan="2">0.492</td>
            <td class="text-center" colspan="2">0.489</td>
            <td class="text-center">0.501</td>
            <td class="text-center">0.494</td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">अमोनिया/यूरिया</td>
            <td class="text-center" colspan="2">मी.टन/मी.टन</td>
            <td class="text-center" colspan="2">0.582</td>
            <td class="text-center" colspan="2">0.582</td>
            <td class="text-center">0.582</td>
            <td class="text-center">0.582</td>
        </tr>
        <tr class="bg-light">
            <td class="text-left fw-bold" colspan="2">बै.लि.अमो.एनर्जी/अमोनिया</td>
            <td class="text-center" colspan="2">मी.कि.कै./मी.टन</td>
            <td class="text-center" colspan="2">8.392</td>
            <td class="text-center" colspan="2">8.354</td>
            <td class="text-center">8.701</td>
            <td class="text-center">8.508</td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">प्लांट एनर्जी/यूरिया</td>
            <td class="text-center" colspan="2">मी.कि.कै./मी.टन</td>
            <td class="text-center" colspan="2">6.500</td>
            <td class="text-center" colspan="2">6.144</td>
            <td class="text-center">6.401</td>
            <td class="text-center">6.304</td>
        </tr>
        <tr class="bg-light">
            <td class="text-left fw-bold" colspan="2">कोयला/यूरिया</td>
            <td class="text-center" colspan="2">मी.टन/मी.टन</td>
            <td class="text-center" colspan="2">0.758</td>
            <td class="text-center" colspan="2">0.275</td>
            <td class="text-center">0.286</td>
            <td class="text-center">0.287</td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">फ्यूल एन.जी./यूरिया</td>
            <td class="text-center" colspan="2">केएसएम3/मी.टन</td>
            <td class="text-center" colspan="2">0.047</td>
            <td class="text-center" colspan="2">0.029</td>
            <td class="text-center">0.028</td>
            <td class="text-center">0.028</td>
        </tr>
        <tr class="bg-light">
            <td class="text-left fw-bold" colspan="2">स्टीम/अमोनिया</td>
            <td class="text-center" colspan="2">मी.टन/मी.टन</td>
            <td class="text-center" colspan="2">2.049</td>
            <td class="text-center" colspan="2">2.158</td>
            <td class="text-center">2.297</td>
            <td class="text-center">2.258</td>
        </tr>
        <tr>
            <td class="text-left fw-bold" colspan="2">स्टीम/यूरिया</td>
            <td class="text-center" colspan="2">मी.टन/मी.टन</td>
            <td class="text-center" colspan="2">1.118</td>
            <td class="text-center" colspan="2">1.094</td>
            <td class="text-center">1.113</td>
            <td class="text-center">1.097</td>
        </tr>
        <tr class="bg-light">
            <td class="text-left fw-bold" colspan="2">कुल पावर/यूरिया</td>
            <td class="text-center" colspan="2">मेगावाट/मी.टन</td>
            <td class="text-center" colspan="2">0.332</td>
            <td class="text-center" colspan="2">0.008</td>
            <td class="text-center">0.010</td>
            <td class="text-center">0.017</td>
        </tr>

        <tr>
            <th class="text-left" colspan="4">उत्पादन की बाधाएं &nbsp;&nbsp; Production Loss</th>
            <th class="text-center" colspan="3">अमोनिया उत्पादन की हानि मी.टन<br>दिन &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; माह &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; वर्ष</th>
            <th class="text-center" colspan="3">यूरिया उत्पादन की हानि मी.टन<br>दिन &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; माह &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; वर्ष</th>
        </tr>
        <tr>
            <td class="text-left" colspan="4">कच्चा माल</td>
            <td class="text-right"><%= r.get("D_LS_RMTLS_AMM") %></td>
            <td class="text-right"><%= r.get("M_LS_RMTLS_AMM") %></td>
            <td class="text-right"><%= r.get("Y_LS_RMTLS_AMM") %></td>
            <td class="text-right"><%= r.get("D_LS_RMTLS_UREA") %></td>
            <td class="text-right"><%= r.get("M_LS_RMTLS_UREA") %></td>
            <td class="text-right"><%= r.get("Y_LS_RMTLS_UREA") %></td>
        </tr>
        <tr class="bg-light">
            <td class="text-left" colspan="4">बाहर से पावर की कमी</td>
            <td class="text-right"><%= r.get("D_LS_POWER_AMM") %></td>
            <td class="text-right"><%= r.get("M_LS_POWER_AMM") %></td>
            <td class="text-right"><%= r.get("Y_LS_POWER_AMM") %></td>
            <td class="text-right"><%= r.get("D_LS_POWER_UREA") %></td>
            <td class="text-right"><%= r.get("M_LS_POWER_UREA") %></td>
            <td class="text-right"><%= r.get("Y_LS_POWER_UREA") %></td>
        </tr>
        <tr>
            <td class="text-left" colspan="4">मेकेनिकल अवरोध</td>
            <td class="text-right"><%= r.get("D_LS_MECH_AMM") %></td>
            <td class="text-right"><%= r.get("M_LS_MECH_AMM") %></td>
            <td class="text-right"><%= r.get("Y_LS_MECH_AMM") %></td>
            <td class="text-right"><%= r.get("D_LS_MECH_UREA") %></td>
            <td class="text-right"><%= r.get("M_LS_MECH_UREA") %></td>
            <td class="text-right"><%= r.get("Y_LS_MECH_UREA") %></td>
        </tr>
        <tr class="bg-light">
            <td class="text-left" colspan="4">इलेक्ट्रीकल अवरोध</td>
            <td class="text-right"><%= r.get("D_LS_ELEC_AMM") %></td>
            <td class="text-right"><%= r.get("M_LS_ELEC_AMM") %></td>
            <td class="text-right"><%= r.get("Y_LS_ELEC_AMM") %></td>
            <td class="text-right"><%= r.get("D_LS_ELEC_UREA") %></td>
            <td class="text-right"><%= r.get("M_LS_ELEC_UREA") %></td>
            <td class="text-right"><%= r.get("Y_LS_ELEC_UREA") %></td>
        </tr>
        <tr>
            <td class="text-left" colspan="4">इन्स्ट्रूमेंट अवरोध</td>
            <td class="text-right"><%= r.get("D_LS_INST_AMM") %></td>
            <td class="text-right"><%= r.get("M_LS_INST_AMM") %></td>
            <td class="text-right"><%= r.get("Y_LS_INST_AMM") %></td>
            <td class="text-right"><%= r.get("D_LS_INST_UREA") %></td>
            <td class="text-right"><%= r.get("M_LS_INST_UREA") %></td>
            <td class="text-right"><%= r.get("Y_LS_INST_UREA") %></td>
        </tr>
        <tr class="bg-light">
            <td class="text-left" colspan="4">प्रोसेस अवरोध</td>
            <td class="text-right"><%= r.get("D_LS_PROC_AMM") %></td>
            <td class="text-right"><%= r.get("M_LS_PROC_AMM") %></td>
            <td class="text-right"><%= r.get("Y_LS_PROC_AMM") %></td>
            <td class="text-right"><%= r.get("D_LS_PROC_UREA") %></td>
            <td class="text-right"><%= r.get("M_LS_PROC_UREA") %></td>
            <td class="text-right"><%= r.get("Y_LS_PROC_UREA") %></td>
        </tr>
        <tr>
            <td class="text-left" colspan="4">नियोजित शटडाउन</td>
            <td class="text-right"><%= r.get("D_LS_SD_AMM") %></td>
            <td class="text-right"><%= r.get("M_LS_SD_AMM") %></td>
            <td class="text-right"><%= r.get("Y_LS_SD_AMM") %></td>
            <td class="text-right"><%= r.get("D_LS_SD_UREA") %></td>
            <td class="text-right"><%= r.get("M_LS_SD_UREA") %></td>
            <td class="text-right"><%= r.get("Y_LS_SD_UREA") %></td>
        </tr>
        <tr class="bg-light">
            <td class="text-left" colspan="4">वार्षिक शटडाउन</td>
            <td class="text-right"><%= r.get("D_LS_AN_SD_AMM") %></td>
            <td class="text-right"><%= r.get("M_LS_AN_SD_AMM") %></td>
            <td class="text-right"><%= r.get("Y_LS_AN_SD_AMM") %></td>
            <td class="text-right"><%= r.get("D_LS_AN_SD_UREA") %></td>
            <td class="text-right"><%= r.get("M_LS_AN_SD_UREA") %></td>
            <td class="text-right"><%= r.get("Y_LS_AN_SD_UREA") %></td>
        </tr>
        <tr>
            <td class="text-left" colspan="4">अन्य अवरोध</td>
            <td class="text-right"><%= r.get("D_LS_OTHER_AMM") %></td>
            <td class="text-right"><%= r.get("M_LS_OTHER_AMM") %></td>
            <td class="text-right"><%= r.get("Y_LS_OTHER_AMM") %></td>
            <td class="text-right"><%= r.get("D_LS_OTHER_UREA") %></td>
            <td class="text-right"><%= r.get("M_LS_OTHER_UREA") %></td>
            <td class="text-right"><%= r.get("Y_LS_OTHER_UREA") %></td>
        </tr>
        <tr>
            <td class="text-center fw-bold bg-light" colspan="4">कुल : &nbsp;&nbsp;&nbsp; Total</td>
            <td class="text-right fw-bold bg-light"><%= r.get("D_LS_TOT_AMM") %></td>
            <td class="text-right fw-bold bg-light"><%= r.get("M_LS_TOT_AMM") %></td>
            <td class="text-right fw-bold bg-light"><%= r.get("Y_LS_TOT_AMM") %></td>
            <td class="text-right fw-bold bg-light"><%= r.get("D_LS_TOT_UREA") %></td>
            <td class="text-right fw-bold bg-light"><%= r.get("M_LS_TOT_UREA") %></td>
            <td class="text-right fw-bold bg-light"><%= r.get("Y_LS_TOT_UREA") %></td>
        </tr>
    </table>

    <div class="status-box">
        <span class="fw-bold" style="font-size: 14px;">संयंत्र की स्थिति &nbsp;&nbsp;&nbsp;&nbsp; Plant Status</span><br>
        <%= r.get("D_TX_STATUS1") != null ? r.get("D_TX_STATUS1") : "" %><br>
        <%= r.get("D_TX_STATUS2") != null ? r.get("D_TX_STATUS2") : "" %><br>
        <%= r.get("D_TX_STATUS3") != null ? r.get("D_TX_STATUS3") : "" %><br>
        <%= r.get("D_TX_STATUS4") != null ? r.get("D_TX_STATUS4") : "" %><br>
        <%= r.get("D_TX_STATUS5") != null ? r.get("D_TX_STATUS5") : "" %>
    </div>

<% } else if(request.getParameter("reportDate") != null) { %>
    <p style="text-align:center; color:red;">No data found for the selected date.</p>
<% } %>

</div>

</body>
</html>