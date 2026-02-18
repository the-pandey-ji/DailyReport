<%@ page contentType="text/html;charset=UTF-8" %>
<%
String reportDate = (String) request.getAttribute("reportdate");
String ldohsdOp = String.valueOf(request.getAttribute("ldohsd_op"));
String coalOp = String.valueOf(request.getAttribute("coal_op"));
String error = String.valueOf(request.getAttribute("error"));
%>

<!DOCTYPE html>
<html>
<head>
<title>MHP Daily Report Data Entry Form</title>

<style>
body{font-family:Arial;background:#eee}
h3{text-align:center;color:red}
table{width:95%;margin:auto;border-collapse:collapse}
td{padding:6px}
input{width:90px}
.header{background:#dcdcdc;padding:10px}
.footer{text-align:center;margin-top:20px}
</style>

<script>
function num(id){
    return parseFloat(document.getElementById(id).value||0);
}

function calcLdohsd(){
    let v = num("ldohsd_op_stk")+num("ldohsd_rct")-num("ldohsd_tfr");
    document.getElementById("ldohsd_cl_stk").value=v.toFixed(3);
}

function calcCoal(){
    let v = num("coal_op_stk")+num("coal_rct")-num("coal_tfr")-num("coal_tfr_ounit");
    document.getElementById("coal_cl_stk").value=v.toFixed(3);
}

function loadDetails(){
    let d=document.getElementById("report_date").value;
    let x=new XMLHttpRequest();
    x.open("GET","materialHandling?action=details&reportdate="+encodeURIComponent(d),true);
    x.onload=function(){
        if(this.responseText.startsWith("N")){
            alert("No data available for selected date");
            return;
        }
        let a=this.responseText.split("?#?");
        let i=0;
        [
          "ldohsd_op_stk","ldohsd_rct","ldohsd_tfr","ldohsd_cl_stk",
          "coal_op_stk","coal_rct","coal_tfr","coal_tfr_ounit","coal_cl_stk"
        ].forEach(id=>{
            document.getElementById(id).value=a[i++];
        });
    }
    x.send();
}
</script>
</head>

<body>

<div class="header">
<b>MHP Daily Report Data Entry Form</b>
</div>

<h3>MHP Daily Report Data Entry Form</h3>

<form method="post" action="materialHandling">

<input type="hidden" name="error" value="<%=error%>">

<table>
<tr>
<td>Report Date</td>
<td><input id="report_date" name="reportdate" value="<%=reportDate%>" onchange="loadDetails()" ></td>
</tr>

<tr>
<td>LDOHSD Opening Stock</td>
<td><input id="ldohsd_op_stk" readonly value="<%=ldohsdOp%>"></td>
<td>LDOHSD Receipt</td>
<td><input id="ldohsd_rct" name="ldohsd_rct" onblur="calcLdohsd()"></td>
<td>LDOHSD Transfer</td>
<td><input id="ldohsd_tfr" name="ldohsd_tfr" onblur="calcLdohsd()"></td>
<td>LDOHSD Closing Stock</td>
<td><input id="ldohsd_cl_stk" name="ldohsd_cl_stk" readonly></td>
</tr>

<tr>
<td>Coal Opening Stock</td>
<td><input id="coal_op_stk" readonly value="<%=coalOp%>"></td>
<td>Coal Receipt</td>
<td><input id="coal_rct" name="coal_rct" onblur="calcCoal()"></td>
<td>Coal Transfer</td>
<td><input id="coal_tfr" name="coal_tfr" onblur="calcCoal()"></td>
<td>Coal Trfr to other Unit</td>
<td><input id="coal_tfr_ounit" name="coal_tfr_ounit" onblur="calcCoal()"></td>
<td>Coal Closing Stock</td>
<td><input id="coal_cl_stk" name="coal_cl_stk" readonly></td>
</tr>
</table>

<div class="footer">
<button type="submit">Save</button>
<button type="button" onclick="location.href='index.jsp'">Exit</button>
</div>

</form>
</body>
</html>
