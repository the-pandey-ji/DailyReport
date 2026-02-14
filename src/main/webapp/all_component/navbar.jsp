<%@ page import="com.entity.User" %>

<!-- TOP ACCENT BAR -->
<div class="container-fluid p-0">
    <div style="height:4px;background:#0b6b3a;"></div>
</div>

<!-- HEADER -->
<div class="container-fluid bg-white shadow-sm py-2">
    <div class="row align-items-center">

        <!-- LEFT: LOGO + BRAND NAME INLINE -->
        <div class="col-md-4 d-flex align-items-center">
            <img src="/NFLGuestHouse/images/nflimage.png"
                 alt="NFL Logo"
                 style="height:70px;"
                 class="mr-3 header-logo">

            <h4 class="mb-0 text-primary header-title">
                <i class="fas fa-building mr-2"></i>
                Guest House Management System
            </h4>
        </div>

        <!-- CENTER: COMPANY NAME -->
        <div class="col-md-4 text-center header-company">
            <div style="line-height:1.3;">
                <h3 class="mb-0 font-weight-bold text-success text-uppercase">
                    National Fertilizers Limited
                </h3>
                <h5 class="mb-0 font-weight-bold text-danger text-uppercase">
                    Panipat Unit
                </h5>
            </div>
        </div>

        <!-- RIGHT: USER PROFILE DROPDOWN -->
        <div class="col-md-4 text-right header-user">
            <%
                User user1 = (User) session.getAttribute("userObj");
                if (user1 != null) {
            %>
                <div class="dropdown d-inline-block">
                    <button class="btn btn-success dropdown-toggle px-5 py-2 user-btn"
                            type="button"
                            id="userDropdown"
                            data-toggle="dropdown"
                            aria-haspopup="true"
                            aria-expanded="false"
                            style="font-size:1.05rem;">
                        <i class="fas fa-user-circle mr-2" style="font-size:1.3rem;"></i>
                        <span class="user-name"><%= user1.getFullName() %></span>
                    </button>

                    <div class="dropdown-menu dropdown-menu-right shadow-sm"
                         aria-labelledby="userDropdown">
                        <a class="dropdown-item" href="userProfile.jsp">
                            <i class="fas fa-id-badge mr-2 text-primary"></i>
                            My Profile
                        </a>

                        <a class="dropdown-item" href="changePassword.jsp">
                            <i class="fas fa-key mr-2 text-warning"></i>
                            Change Password
                        </a>

                        <div class="dropdown-divider"></div>

                        <a class="dropdown-item text-danger"
                           data-toggle="modal"
                           data-target="#exampleModalCenter">
                            <i class="fas fa-sign-out-alt mr-2"></i>
                            Logout
                        </a>
                    </div>
                </div>
            <%
                }
                else {
                	response.sendRedirect("index.jsp");
                }
            %>
        </div>

    </div>
</div>

<!-- SECOND ACCENT BAR -->
<div class="container-fluid p-0">
    <div style="height:4px;background:linear-gradient(90deg,#0b6b3a,#1e8f5a);"></div>
</div>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-dark bg-custom shadow-sm">
    <a class="navbar-brand" href="home.jsp">
        <i class="fas fa-home"></i>
    </a>

    <button class="navbar-toggler" type="button" data-toggle="collapse"
            data-target="#navbarSupportedContent">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">

        <!-- LEFT MENU -->
        <ul class="navbar-nav mr-auto">

            <li class="nav-item active">
                <a class="nav-link" href="home.jsp">
                    <i class="fas fa-chart-line mr-1"></i> Dashboard
                </a>
            </li>

            <li class="nav-item active">
                <a class="nav-link" href="viewRooms">
                    <i class="fas fa-bed mr-1"></i> Room Master
                </a>
            </li>

            <li class="nav-item active">
                <a class="nav-link" href="newBooking">
                    <i class="fas fa-calendar-plus mr-1"></i> New Booking
                </a>
            </li>

            <li class="nav-item active">
                <a class="nav-link" href="<%=request.getContextPath()%>/checkin">
                    <i class="fas fa-sign-in-alt mr-1"></i> Check-In / Check-Out
                </a>
            </li>

            <li class="nav-item active">
                <a class="nav-link" href="billing/bill_list.jsp">
                    <i class="fas fa-file-invoice mr-1"></i> Billing
                </a>
            </li>

            <li class="nav-item active">
                <a class="nav-link" href="reports/reports.jsp">
                    <i class="fas fa-chart-bar mr-1"></i> Reports
                </a>
            </li>

        </ul>

        <!-- RIGHT MENU -->
        <div class="form-inline navbar-actions">
            <a href="changePassword.jsp"
               class="btn btn-outline-light btn-sm mr-2">
                <i class="fas fa-key"></i> Change Password
            </a>

            <a href="helpline.jsp"
               class="btn btn-outline-light btn-sm">
                <i class="fas fa-phone-alt"></i> Contact Us
            </a>
        </div>
    </div>
</nav>

<!-- LOGOUT MODAL -->
<div class="modal fade" id="exampleModalCenter" tabindex="-1">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header border-0">
        <button type="button" class="close" data-dismiss="modal">
          <span>&times;</span>
        </button>
      </div>

      <div class="modal-body text-center">
        <h5 class="mb-4">Are you sure you want to logout?</h5>
        <button class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <a href="logout" class="btn btn-danger ml-3">Logout</a>
      </div>
    </div>
  </div>
</div>
