<script>
	const urlAPI = process.env.URLAPI;
	let email;
	let password;
    let error = false;

	import Cookies from 'js-cookie';
	import { goto } from '$app/navigation';

	const loginRestaurant = async () => {
		let data = await fetch(urlAPI + '/auth/restaurant/login', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				login: email,
				password
			})
		});
		if (data.status === 200) {
			let response = await data.json();
			// write to a cookie
			Cookies.set('token', response.accessToken);
            redirect()
		} else {
			error = true
		}
		password = '';
	};

	async function getRestaurantInfo() {
		let response = await fetch(urlAPI + '/auth/restaurant/getRestaurantInfo', {
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
		let userInfo = await getRestaurantInfo();
		if (userInfo.role === 'ROLE_RESTAURANT') {
			goto('/product')
		}
		else {
			goto('/restaurant')
		}
	}
</script>

<div class="p-4 sm:ml-64">
	<main class="centered">
		<h1 class="bold">Connectez-vous</h1>
		<br />
		<br />

		<form on:submit|preventDefault={loginRestaurant}>
			<input
				type="email"
				id="email"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
				placeholder="Adresse mail"
				required
			/>

			<label>
				<input
					type="password"
					id="password"
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
		</form>
	</main>
</div>
