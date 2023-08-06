$(document).ready(function () {
  function getAllCustomers() {
    $.ajax({
      url: "http://localhost:8080/api/v1/getAllCustomers",
      type: "GET",
      dataType: "json",
      success: function (data) {
        displayCustomers(data);
      },
      error: function (error) {
        console.error("Failed to fetch customers:", error);
      },
    });
  }

  function displayCustomers(customers) {
    var tableBody = $("#customerTableBody");
    tableBody.empty();

    customers.forEach(function (customer) {
      var row = $("<tr>");
      row.append($("<td>").text(customer.first_name));
      row.append($("<td>").text(customer.last_name));
      row.append($("<td>").text(customer.street));
      row.append($("<td>").text(customer.address));
      row.append($("<td>").text(customer.city));
      row.append($("<td>").text(customer.state));
      row.append($("<td>").text(customer.email));
      row.append($("<td>").text(customer.phone));

      var actionsCell = $("<td>");
      actionsCell.append(
        $("<button>")
          .addClass("btn btn-sm btn-primary mr-2")
          .text("Edit")
          .attr("data-customer-id", customer.uuid)
          .click(editCustomer)
      );
      actionsCell.append(
        $("<button>")
          .addClass("btn btn-sm btn-danger")
          .text("Delete")
          .attr("data-customer-id", customer.uuid)
          .click(deleteCustomer)
      );
      row.append(actionsCell);

      tableBody.append(row);
    });
  }

  function editCustomer() {
    var customerId = $(this).attr("data-customer-id");

    $.ajax({
      url: "http://localhost:8080/api/v1/getAllCustomers", 
      type: "GET",
      dataType: "json",
      success: function (customers) {
        
        var customer = customers.find(function (c) {
          return c.uuid === customerId;
        });

        if (customer) {
          $("#editFirstName").val(customer.first_name);
          $("#editLastName").val(customer.last_name);
          $("#editEmail").val(customer.email);
          $("#editStreet").val(customer.street);
          $("editState").va;
          $("#editAddress").val(customer.address);
          $("#editCity").val(customer.city);
          $("#editPhone").val(customer.phone);

          $("#editModal").modal("show");

          $("#saveChanges").attr("data-customer-id", customerId);
        }
      },
      error: function (error) {
        console.error("Failed to fetch customer details:", error);
      },
    });
  }

  $("#saveChanges").click(function () {
    var customerId = $(this).attr("data-customer-id");

    var updatedCustomer = {
      first_name: $("#editFirstName").val(),
      last_name: $("#editLastName").val(),
      street: $("#editStreet").val(),
      address: $("#editAddress").val(),
      city: $("#editCity").val(),
      state: $("#editState").val(),
      email: $("#editEmail").val(),
      phone: $("#editPhone").val(),
    };

   
    $.ajax({
      url: "http://localhost:8080/api/v1/updateCustomer/" + customerId,
      type: "POST",
      data: JSON.stringify(updatedCustomer),
      contentType: "application/json",
      success: function (data) {
        $("#editModal").modal("hide");
        getAllCustomers();
        alert("Customer details updated successfully");
      },

      error: function (error) {
        console.error("Failed to update customer details:", error);
      },
    });
  });



  function deleteCustomer() {
    var customerId = $(this).attr("data-customer-id");

    $.ajax({
      url: "http://localhost:8080/api/v1/deleteCustomer?uuid=" + customerId,
      type: "POST",
      success: function (data) {
        $(this).closest("tr").remove();
        alert("Customer deleted successfully");
        getAllCustomers();
      },
      error: function (error) {
        console.error("Failed to delete customer:", error);
      },
    });
  }

  getAllCustomers();
});
function goToAddCustomer() {
    window.location.href = "./addCustomer.html";
}