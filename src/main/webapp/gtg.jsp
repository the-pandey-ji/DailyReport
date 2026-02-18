<%@ page contentType="text/html;charset=UTF-8" %>
<%
String reportDate = (String)request.getAttribute("report_date");
if(reportDate == null) reportDate = "";
String error = request.getAttribute("error")==null?"":request.getAttribute("error").toString();
%>

<!DOCTYPE html>
<html>
<head>
<title>GTG Daily Report Data Entry Form</title>

<style>
body{font-family:Arial;background:#eee}
h3{text-align:center;color:#800000;margin-top:20px}
table{width:95%;margin:auto;border-collapse:collapse}
td{padding:6px}
input{width:90px}
.header{background:#b7d8ea;padding:6px;font-weight:bold}
.footer{text-align:center;margin:20px}
</style>

<script>
function checkno(el){
    if(el.value==="" ) return;
    if(isNaN(el.value)){
        alert("Please enter numeric value");
        el.value="";
        el.focus();
    }
}

function calculateGTGHRSG(){
    var a = parseFloat(document.getElementById("d_ng_consm_gtg").value || 0);
    var b = parseFloat(document.getElementById("d_ng_consm_hrsg").value || 0);
    document.getElementById("d_ng_consm_gtg_hrsg").value = (a+b).toFixed(3);
}

function getDetail(){
    var d = document.getElementById("report_date").value;
    if(d===""){
        alert("Please enter date");
        return;
    }

    var x = new XMLHttpRequest();
    x.open("GET","gtg?action=details&report_date="+encodeURIComponent(d),true);
    x.onload=function(){
        var arr=this.responseText.split("?#?");
        if(arr[0]==="N"){
            alert("No Data is available for entered date");
            return;
        }
        document.getElementById("d_ng_consm_gtg").value=arr[0];
        document.getElementById("d_ng_consm_hrsg").value=arr[1];
        document.getElementById("d_ng_consm_gtg_hrsg").value=arr[2];
        document.getElementById("d_bfw_imp_hrsg").value=arr[3];
        document.getElementById("d_stm_exp_100k_amm").value=arr[4];
        document.getElementById("d_stm_exp_35").value=arr[5];
        document.getElementById("d_stm_exp_75").value=arr[6];
        document.getElementById("d_gtg_running_hrs").value=arr[7];
        document.getElementById("d_hrsg_running_hrs").value=arr[8];
    }
    x.send();
}

function save(){
    document.GTGEntry.jmethod.value="saveRecords";
    document.GTGEntry.submit();
}

function setForm(){
    if("<%=error%>"==="N"){
        alert("You are not authorized");
        document.querySelectorAll("input").forEach(i=>i.disabled=true);
    }
}
</script>
</head>

<body onload="setForm()">

<div class="header">
Department : IT
<span style="float:right">Date : <%=reportDate%></span>
</div>

<h3>GTG Daily Report Data Entry Form</h3>

<form name="GTGEntry" method="post" action="gtg">

<input type="hidden" name="jmethod">
<input type="hidden" name="error" id="error" value="<%=error%>">

<table>
<tr>
<td><b>Report Date</b></td>
<td><input id="report_date" name="report_date" value="<%=reportDate%>" onchange="getDetail()"></td>
</tr>

<tr>
<td>NG Consumption GTG</td>
<td><input id="d_ng_consm_gtg" name="d_ng_consm_gtg" onblur="checkno(this);calculateGTGHRSG()"></td>

<td>NG Consumption HRSG</td>
<td><input id="d_ng_consm_hrsg" name="d_ng_consm_hrsg" onblur="checkno(this);calculateGTGHRSG()"></td>

<td>NG Consumption GTG HRSG</td>
<td><input id="d_ng_consm_gtg_hrsg" name="d_ng_consm_gtg_hrsg" readonly></td>
</tr>

<tr>
<td>BFW Import HRSG</td>
<td><input id="d_bfw_imp_hrsg" name="d_bfw_imp_hrsg" onblur="checkno(this)"></td>

<td>Steam Export 100k Amm</td>
<td><input id="d_stm_exp_100k_amm" name="d_stm_exp_100k_amm" onblur="checkno(this)"></td>

<td>Steam Export 3.5 Kg</td>
<td><input id="d_stm_exp_35" name="d_stm_exp_35" onblur="checkno(this)"></td>
</tr>

<tr>
<td>Steam Export 7.5 Kg</td>
<td><input id="d_stm_exp_75" name="d_stm_exp_75" onblur="checkno(this)"></td>

<td>GTG Running Hrs</td>
<td><input id="d_gtg_running_hrs" name="d_gtg_running_hrs" onblur="checkno(this)"></td>

<td>HRSG Running Hours</td>
<td><input id="d_hrsg_running_hrs" name="d_hrsg_running_hrs" onblur="checkno(this)"></td>
</tr>
</table>

<div class="footer">
<button type="button" onclick="save()">Save</button>
<button type="button" onclick="location.href='index.jsp'">Exit</button>
</div>

</form>
</body>
</html>
