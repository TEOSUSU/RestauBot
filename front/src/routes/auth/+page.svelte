<script>
	let login = "";
	let password = "";
	let error = false;

	import Cookies from 'js-cookie';
	import { goto } from '$app/navigation';

	const logIn = async (event) => {
		event.preventDefault();
		let data = await fetch('http://localhost:8080/auth/login', {
			method: 'POST',
			headers: {
				Authentification:'Bearer Token',
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				"login": login,
				"password": password
			})
		});
		if (data.status === 200) {
			let response = await data.json();
			// write to a cookie
			Cookies.set('token', response.accessToken);
			redirect();
		} else {
			error = true;
		}
		password = '';
	};

	async function getUserInfo() {
		let response = await fetch('http://localhost:8080/auth/getUserInfo', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				Authorization: 'Bearer ' + Cookies.get('token')
			}
		})
		let data = await response.json();
		return data
	}

	async function redirect() {
		let userInfo = await getUserInfo();
		if (userInfo.role === 'ROLE_CUSTOMER') {
			goto('http://localhost:5173/RestaurantMenu?restaurant=1')
		}
		else if (userInfo.role === "ROLE_RESTAURANT") {
      console.log(userInfo)
			goto(`http://localhost:5173/RestaurantMenu?restaurant=${userInfo.idUser}`)
		}
		else {
			goto('http://localhost:8080/auth')
		}
	}
</script>

<div class="p-4 sm:ml-64">
	<main class="centered">
		<h1 class="bold">Connectez-vous</h1>
		<br />
		<br />

		<form on:submit|preventDefault={(e) => logIn(e)}>
			<input
				type="email"
				bind:value={login}
				id="email"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
				placeholder="Adresse mail"
				required
			/>

			<label>
				<input
					type="password"
					id="password"
					bind:value={password}
					class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
					placeholder="•••••••••"
					required
				/>
				<!-- svelte-ignore a11y-missing-attribute -->
				<img
					class="imgPassword"
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

			{#if error}
				<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
					import Swal from 'sweetalert2';

					Swal.fire({
						title: 'Aïe...',
						text: 'Les mots de passe ne correspondent pas, Vérifiez vos informations !',
						icon: 'error',
						confirmButtonText: 'Fermer',
						confirmButtonColor: 'green'
					});
				</script>
			{/if}

			<button
				type="submit"
				class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
				>Se connecter</button
			>
		</form>

		<br>
		<p class="linkAccount">
			<a href="http://localhost:5173/restaurantCreation">Vous n'avez pas de compte ? <br /> Inscrivez-vous</a>
		</p>

	</main>
</div>

<style>
	.centered {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100vh;
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

	.bold {
		font-weight: bold;
		font-size: 30px;
	}

	a {
		cursor: pointer;
		text-align: center;
	}

	.linkAccount {
		text-align: center;
	}

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
</style>
