<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            margin: 0;
            padding: 20px;
        }

        h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        .container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
        }

        /* Anchor behaves like a block */
        .card-link {
            text-decoration: none;
            color: inherit;
            display: block;
        }

        .box {
            background: #ffffff;
            border-radius: 10px;
            padding: 30px;
            text-align: center;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transition: transform 0.2s, box-shadow 0.2s;
            cursor: pointer;
        }

        .box:hover {
            transform: translateY(-6px);
            box-shadow: 0 6px 16px rgba(0,0,0,0.2);
        }

        .box h3 {
            margin: 0;
            font-size: 18px;
            color: #2c3e50;
        }
    </style>
</head>

<body>

<h2>Plant Modules</h2>

<div class="container">

    <a href="dashboard.jsp" class="card-link">
        <div class="box">
            <h3>MHP</h3>
        </div>
    </a>

    <a href="ammonia.jsp" class="card-link">
        <div class="box">
            <h3>Ammonia</h3>
        </div>
    </a>

    <a href="urea" class="card-link">
        <div class="box">
            <h3>Urea</h3>
        </div>
    </a>

    <a href="steam.jsp" class="card-link">
        <div class="box">
            <h3>Steam</h3>
        </div>
    </a>

    <a href="gtg.jsp" class="card-link">
        <div class="box">
            <h3>GTG</h3>
        </div>
    </a>

    <a href="captivePower.jsp" class="card-link">
        <div class="box">
            <h3>Captive Power</h3>
        </div>
    </a>

    <a href="powerReport.jsp" class="card-link">
        <div class="box">
            <h3>Power Report</h3>
        </div>
    </a>

    <a href="offsites" class="card-link">
        <div class="box">
            <h3>Offsites & Utilities</h3>
        </div>
    </a>



</div>

</body>
</html>
