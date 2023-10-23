<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
    // JavaScript code to handle form submission can be added here
    import Swal from 'sweetalert2';

    const urlAPI = 'http://localhost:8080';
    
    let password;
    let companyName;
    let address;
    let city;
    let number;
    let phone;
    let mail;
    let confirm_password;
    let fidelity;
   
    async function restaurantCreation() {
        console.log(fidelity); // Ajout du message

        const formData = {
                idRestaurant: "",
                companyName: companyName,
                address: address,
                zipcode: number,
                city: city,
                phone: phone,
                mail: mail,
                fidelity: fidelity,
                // password: password,
                // confirm_password: confirm_password
        }
        try {
            
            const createResponse = await fetch(urlAPI + `/api/restaurant`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData)
            });
            if (createResponse.ok) {
                        // Réinitialisez les champs du formulaire
                        // ...

                Swal.fire({
                    title: 'Bien joué !',
                    text: 'Votre inscription a été validée avec succès !',
                    icon: 'success',
                    confirmButtonText: 'Fermer',
                    confirmButtonColor: 'green'
                });
            }
        }
        catch (error) {
            console.error('Une erreur inattendue est survenue :', error);
        }
	}

</script>
<head>
    <title>Page Inscription Restaurateur</title>
</head>

<body>
    <main class="centered">
        <h1>Page Inscription Restaurateur</h1>
        <div id="formContainer">
            <form on:submit|preventDefault={restaurantCreation}>
                <div>
                    <label for="restaurant_name">Nom de l'enseigne</label>
                    <input
                        type="text"
                        bind:value={companyName}
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
                        bind:value={address}
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
                        bind:value={city}
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
                        bind:value={number}
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
                        bind:value={phone}
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
                        bind:value={mail}
                        id="mail"
                        name="mail"
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                        required
                    />
                </div>
                <div>
                    <p>Fidélité</p>
                    <div  class="flex float-left mb-4">
                        
                        <input  type="checkbox" 
                                bind:checked={fidelity} 
                                id="fidelity"  
                                class="flex float-left mb-4 text-blue-600 bg-blue-100 border-gray-300 rounded focus:ring-blue-500"
                            >
                    </div>
                </div>
               
                <label for="password">Mot de passe
                    <input
                        type="password"
                        bind:value={password}
                        id="password"
                        placeholder="Mot de passe"
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                        required
                    />
                    <img
                        class="imgPassword"
                        alt="eye"
                        src="../src/images/oeil-ouvert.png"
                        id="oeil"
                        onClick="changer()"
                    />
                </label>
                

                <script>

                    e = true;
                    function changer() {
                        if (e) {
                            document.getElementById('password').setAttribute('type', 'text');
                            document.getElementById('oeil').src = '../src/images/oeil-ferme.png';
                            e = false;
                        } else {
                            document.getElementById('password').setAttribute('type', 'password');
                            document.getElementById('oeil').src = '../src/images/oeil-ouvert.png';
                            e = true;
                        }
                    }
                </script>

                
                <div>
                    <label for="confirm_password">Confirmer le mot de passe</label>
                    <input
                        type="password"
                        bind:value={confirm_password}
                        id="confirm_password"
                        name="confirm_password"
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                        required
                    />
                </div>
                <button type="submit"   class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">
                    Valider
                </button>
            </form>
        </div>
    </main>   
</body>










<style>
	.centered {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100vh;
        padding-top:15% ;
	}

	form {
		display: flex;
		flex-direction: column;
		gap: 1rem;
		align-items: center;
	}

	h1 {
		text-align: center;
	}

	/* .bold {
		font-weight: bold;
		font-size: 30px;
	} */

	/* u {
		cursor: pointer;
		text-align: center;
	} */

	/* .linkAccount {
		text-align: center;
	} */

	label .imgPassword {
		display: flex;
		align-items: center;
		position: absolute;
		top: 50%;
		right: 20px;
		transform: translateY(-50%);
		width: 20px;
		transition: all 0.2s;
		cursor: pointer;
	}

	label {
		position: relative;
		width: 100%;
	}

	/* Styles pour la modal */
	/* .modal {
		display: none;
		position: fixed;
		z-index: 1;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.7);
	}

	.modal-content {
		background-color: #fff;
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		padding: 20px;
		border-radius: 5px;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
		text-align: center;
	} */

	/* .close {
		position: absolute;
		top: 0;
		right: 0;
		padding: 10px;
		cursor: pointer;
	} */
</style>

