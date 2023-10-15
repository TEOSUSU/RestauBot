<script>
    	let firstName = '';
        let lastName = '';
        let phone = '';
        let email = '';
        let confirmEmail = '';
        let password = '';
        let confirmPassword = '';
</script>
<div>
    <h1>Page Inscription Restaurateur</h1>
    <div>
        <p>Nom de l'enseigne</p>
        <input
				type="text"
				id="establishment_name"
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 "
				required
			/>
    </div>
    <div>
        <p>Nom de l'enseigne</p>
        <input
                type="text"
                id="adress"
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                required
			/>
    </div>
    <div>
        <p>Ville</p>
        <input
                type="text"
                id="city"
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 "
                required
			/>
    </div>
    <div>
        <p>Code postal</p>
        <input
                type="text"
                id="number"
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                required
			/>
    </div>
    <div>
        <p>Téléphone</p>
        <input
                type="tel"
                id="phone"
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                required
			/>
    </div>
    <div>
        <p>Addresse email</p>
        <input
                type="email"
                id="mail"
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 "
                required
			/>
    </div>
    <div>
        <p>Mot de passe</p>
        <input
                type="password"
                id="password"
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 "
                bind:value={password}
                required
            />
    </div>
    
    <div>
        <p>Confirmer le mot de passe </p>
        <input
                type="password"
                id="confirm_password"
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                bind:value={confirmPassword}
                required
            /> 
    </div>
			
</div>
