
document.getElementById('customerForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const firstName = document.getElementById('first_name').value;
    const lastName = document.getElementById('last_name').value;
    const street = document.getElementById('street').value;
    const address = document.getElementById('address').value;
    const city = document.getElementById('city').value;
    const state = document.getElementById('state').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;

    const customerData = {
        first_name: firstName,
        last_name: lastName,
        street: street,
        address: address,
        city: city,
        state: state,
        email: email,
        phone: phone
    };

    createCustomerAPI(customerData);
});

function createCustomerAPI(customerData) {
    const url = 'http://localhost:8080/api/v1/createCustomer'; 

    $.ajax({
        url: url,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(customerData),
        success: function(response) {
            alert("Successfully created customer")
            window.location.href = "./Home.html";
        },
        error: function(xhr) {
            if (xhr.status === 400) {
                document.getElementById('result').innerText = 'Failed to create customer. Please check the data.';
            } else {
                document.getElementById('result').innerText = 'Failed to create customer: ' + xhr.status;
            }
        }
    });
}
