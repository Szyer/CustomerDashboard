document.getElementById('loginForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const loginId = document.getElementById('loginId').value;
    const password = document.getElementById('password').value;

    try {
        const response = await loginUser(loginId, password);
        console.log('Login Successful', response);
        alert("Logged in!")
     
    } catch (error) {
        console.error('Login Failed', error);
    }
});

async function loginUser(loginId, password) {
    const loginUrl = 'http://localhost:8080/api/v1/login';
    const requestBody = {
        login_id: loginId,
        password: password
    };

    const response = await fetch(loginUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    });

    if (!response.ok) {
        throw new Error('Login failed: ' + response.status);
    }

    return response.json();
}
