<!DOCTYPE html>
<script>
    // JavaScript code to handle form submission can be added here
    const urlAPI = 'http://localhost:5173';
    
    async function restaurantCreation(data) {
        try {
            const response = await fetch(urlAPI + `api/restaurant`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + Cookies.get('token')
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                // Handle success
                console.log('Restaurant created successfully');
            } else {
                // Handle errors
                console.error('Failed to create restaurant');
            }
        } catch (error) {
            console.error('An error occurred:', error);
        }
    }

    function submitForm() {
        console.log("test submit form")
        const restaurant_name = document.getElementById('restaurant_name').value;
        const address = document.getElementById('address').value;
        const city = document.getElementById('city').value;
        const number = document.getElementById('number').value;
        const phone = document.getElementById('phone').value;
        const mail = document.getElementById('mail').value;
        const password = document.getElementById('password').value;
        const confirm_password = document.getElementById('confirm_password').value;

        const data = {
            idRestaurant: "",
            companyName: restaurant_name,
            address: address,
            city: city,
            number: number,
            phone: phone,
            mail: mail,
            password: password,
            confirm_password: confirm_password
        };

        restaurantCreation(data);
    }

</script>
<head>
    <title>Page Inscription Restaurateur</title>
</head>
<body>
    <h1>Page Inscription Restaurateur</h1>
    <div id="formContainer">
        <form id="registrationForm">
            <div>
                <label for="restaurant_name">Nom de l'enseigne</label>
                <input
                    type="text"
                    id="restaurant_name"
                    name="restaurant_name"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                    required
                />
            </div>
            <div>
                <label for="adress">Adresse</label>
                <input
                    type="text"
                    id="adress"
                    name="adress"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                    required
                />
            </div>
            <div>
                <label for="city">Ville</label>
                <input
                    type="text"
                    id="city"
                    name="city"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                    required
                />
            </div>
            <div>
                <label for="number">Code postal</label>
                <input
                    type="text"
                    id="number"
                    name="number"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                    required
                />
            </div>
            <div>
                <label for="phone">Téléphone</label>
                <input
                    type="tel"
                    id="phone"
                    name="phone"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                    required
                />
            </div>
            <div>
                <label for="mail">Adresse email</label>
                <input
                    type="email"
                    id="mail"
                    name="mail"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                    required
                />
            </div>
            <div>
                <label for="password">Mot de passe</label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                    required
                />
            </div>
            <div>
                <label for="confirm_password">Confirmer le mot de passe</label>
                <input
                    type="password"
                    id="confirm_password"
                    name="confirm_password"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                    required
                />
            </div>
            <button type="submit" onclick="submitForm()" class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">
                Valider
            </button>
        </form>
    </div>
</body>

