<%@ page contentType="text/html;charset=UTF-8" %>
<%
String reportDate = request.getAttribute("tx_date")==null?"":request.getAttribute("tx_date").toString();
String signAuth   = request.getAttribute("sign_auth")==null?"":request.getAttribute("sign_auth").toString();
String signName   = request.getAttribute("sign_name")==null?"":request.getAttribute("sign_name").toString();
String signDesg   = request.getAttribute("sign_desg")==null?"":request.getAttribute("sign_desg").toString();
%>

<!DOCTYPE html>
<html>
<head>
<title>Telex Status Entry</title>

<style>
body{
    font-family:Segoe UI, Arial;
    background:#f4f6f8;
    margin:0;
}
h2{
    text-align:center;
    color:#8b0000;
    margin:15px 0;
}
.container{
    width:55%;
    margin:auto;
    background:#fff;
    padding:15px;
    box-shadow:0 0 10px rgba(0,0,0,.1);
}
.header{
    background:#b7d8ea;
    padding:10px;
    font-weight:bold;
}
.section-title{
    background:#e6eef5;
    padding:8px;
    margin-top:15px;
    font-weight:bold;
    border-left:5px solid #8b0000;
}
table{
    width:100%;
    border-collapse:collapse;
}
td{
    padding:6px;
}
label{
    font-weight:600;
}
input, select{
    width:98%;
    padding:6px;
    box-sizing:border-box;
}
input[readonly]{
    background:#f1f1f1;
}
.status-table td{
    border:1px solid #ccc;
}
.footer{
    text-align:center;
    margin:20px 0;
}
button{
    padding:8px 20px;
    font-size:14px;
    margin:0 5px;
    cursor:pointer;
}
</style>

<script>
/* VARCHAR2(100) safety */
function checkLen(el){
    if(el.value.length > 100){
        alert("Maximum 100 characters allowed");
        el.value = el.value.substring(0,100);
    }
}

/* Load existing data */
function loadData(){
    var d=document.getElementById("tx_date").value;
    if(d==="") return;

    var x=new XMLHttpRequest();
    x.open("GET","telex?action=fetch&tx_date="+encodeURIComponent(d),true);
    x.onload=function(){
        if(this.responseText.startsWith("N")){
            clearStatus();
            return;
        }
        var a=this.responseText.split("?#?");
        document.getElementById("sign_auth").value=a[0]||"";
        document.getElementById("sign_name").value=a[1]||"";
        document.getElementById("sign_desg").value=a[2]||"";

        for(var i=1;i<=16;i++)
            document.getElementById("s"+i).value=a[i+2]||"";
    }
    x.send();
}

/* Load sign details */
function loadSign(){
    var s=document.getElementById("sign_auth").value;
    if(s===""){
        document.getElementById("sign_name").value="";
        document.getElementById("sign_desg").value="";
        return;
    }

    var x=new XMLHttpRequest();
    x.open("GET","telex?action=sign&sign_auth="+s,true);
    x.onload=function(){
        var a=this.responseText.split("?#?");
        document.getElementById("sign_name").value=a[0]||"";
        document.getElementById("sign_desg").value=a[1]||"";
    }
    x.send();
}

function clearStatus(){
    for(var i=1;i<=16;i++)
        document.getElementById("s"+i).value="";
}
</script>
</head>

<body>

<div class="header">
Department : IT
<span style="float:right">Date : <%=reportDate%></span>
</div>


<h2>Daily Telex Status Report</h2>

<div class="container">

<form method="post" action="telex">

<!-- ================= HEADER INFO ================= -->
<div class="section-title">Header Information</div>
<table>
<tr>
    <td width="20%"><label>Telex Date</label></td>
    <td width="30%">
        <input type="text" id="tx_date" name="tx_date"
               value="<%=reportDate%>" onchange="loadData()">
    </td>
    <td width="20%"><label>Signing Authority</label></td>
    <td width="30%">
        <select id="sign_auth" name="sign_auth" onchange="loadSign()">
            <option value="">-- Select --</option>
            <%
            java.util.List list=(java.util.List)request.getAttribute("signList");
            if(list!=null)
             for(Object o:list){
               String[] r=(String[])o;
            %>
            <option value="<%=r[0]%>"><%=r[0]%></option>
            <% } %>
        </select>
    </td>
</tr>

<tr>
    <td><label>Name</label></td>
    <td><input id="sign_name" readonly value="<%=signName%>"></td>
    <td><label>Designation</label></td>
    <td><input id="sign_desg" readonly value="<%=signDesg%>"></td>
</tr>
</table>

<!-- ================= STATUS TABLE ================= -->
<div class="section-title">Telex Status Details</div>

<table class="status-table">
<tr>
<%
for(int i=1;i<=16;i++){
%>
    <td width="15%">
        <label>Status <%=i%></label>
         </td>
         <td>
        <input id="s<%=i%>" name="s<%=i%>" maxlength="100"
               onkeyup="checkLen(this)">
    </td>
<%
    if(i%1==0) out.println("</tr><tr>");
}
%>
</tr>
</table>

<div class="footer">
    <button type="submit">Save</button>
    <button type="button" onclick="location.reload()">Clear</button>
    <button type="button" onclick="location.href='index.jsp'">Exit</button>
</div>

</form>
</div>

</body>
</html>
