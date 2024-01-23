<script>
	import { goto } from '$app/navigation';
	import { onMount } from 'svelte';
	import { loadStripe } from '@stripe/stripe-js';
	import { Elements, PaymentElement, LinkAuthenticationElement, Address } from '$lib';
	import { sessionStorage } from '../../stores/stores.js';
	import Cookies from 'js-cookie';
	import Swal from 'sweetalert2';
	import { PUBLIC_STRIPE_KEY } from '$env/static/public';
	export let data;
	import { getContext } from 'svelte';
	import { selectedDate } from '../../stores/stores.js'; // Adjust the path accordingly

	//date of recepetion selected previously in the cart
	let selectedDateValue = getContext('selectedDate') || $selectedDate;

	let userInfo = data.userInfo;

	let stripe = null;
	let clientSecret = null;
	let error = null;
	let elements;
	let processing = false;
	let total = 0;
	let cartData = [];
	let amount = 0;

	const headersList = {
		'Content-Type': 'application/json',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	onMount(async () => {
		if (!userInfo || !userInfo.role) {
			// Rediriger vers la page de connexion
			goto('/auth');
		}
		stripe = await loadStripe(PUBLIC_STRIPE_KEY);

		if (!import.meta.env.SSR) {
			// Récupérer les données actuelles du panier depuis le stockage de session
			cartData = $sessionStorage || [];
		}
		updateTotal();
		amount = (total * 100).toFixed(0);
		clientSecret = await createPaymentIntent(amount);
	});

	async function createPaymentIntent(amount) {
		let body = JSON.stringify({"amount": amount });
		const response = await fetch('/paiement/paiement-intent', {
			method: 'POST',
			headers: {
				'content-type': 'application/json'
			},
			body: body
		});
		const { clientSecret } = await response.json();
		return clientSecret;
	}

	async function submit() {
		// avoid processing duplicates
		if (processing) return;

		processing = true;

		// confirm payment with stripe
		const result = await stripe.confirmPayment({
			elements,
			redirect: 'if_required'
		});

		if (result.error) {
			// payment failed, notify user
			error = result.error;
			processing = false;
		} else {
			// payment succeeded, redirect to "thank you" page
			finalizeOrder();
			setTimeout(() => {
				goto('/customerHistory');
			}, 5000);
		}
	}

	function updateTotal() {
		total = cartData.reduce((acc, product) => acc + product.quantity * product.price, 0);
	}

	async function finalizeOrder() {
		if (total < 10000) {
			try {
				const assignedDish = [];
				const assignedMenu = [];
				cartData.forEach((item) => {
					for (let i = 0; i < item.quantity; i++) {
						if (item.selectedDishes) {
							for (let dishId in item.selectedDishes) {
								assignedDish.push({ idDish: item.selectedDishes[dishId].idDish });
							}
							assignedMenu.push({ idMenu: parseInt(item.id.slice(4)) });
						} else {
							assignedDish.push({ idDish: item.id });
						}
					}
				});

				updateTotal();

				let currentDate = new Date();

				// Divisez la chaîne pour obtenir les heures et les minutes
				let [hours, minutes] = selectedDateValue.split(':').map(Number);

				// Définissez les heures et les minutes sur la date actuelle
				currentDate.setHours(hours, minutes, 0, 0);

				// Formatez la date 
				const formattedDate = new Intl.DateTimeFormat('fr-FR', {
					year: 'numeric',
					month: 'numeric',
					day: 'numeric',
					hour: 'numeric',
					minute: 'numeric',
					second: 'numeric',
					timeZone: 'Europe/Paris'
				}).format(currentDate);
        
		let requestBody;
		if(cartData[0]){
			if(cartData[0].idUser){
				 requestBody = {
					total: total.toFixed(2),
					paid: false,
					collected: false,
					orderTime: formattedDate,
					collectTime: null,
					customer: {
						idUser: userInfo.idUser
					},
					assignedDish: assignedDish,
					assignedMenu: assignedMenu,
					restaurant: {
						idUser: cartData[0].idUser
					}
				};
			}

			else {
				 requestBody = {
					total: total.toFixed(2),
					paid: false,
					collected: false,
					orderTime: formattedDate,
					collectTime: null,
					customer: {
						idUser: userInfo.idUser
					},
					assignedDish: assignedDish,
					assignedMenu: assignedMenu,
					restaurant: {
						idUser: cartData[0].idRestaurant
					}
				};
	
			}
		}
				


				const response = await fetch('http://localhost:8080/api/purchases/create', {
					method: 'POST',
					headers: headersList,
					body: JSON.stringify(requestBody)
				});

				// Effacer le panier après la finalisation de la commande
				cartData = [];
				$sessionStorage = cartData;
				updateTotal();

				Swal.fire({
					title: 'Commande validée !',
					text: 'Votre commande a été envoyé au restaurant !',
					icon: 'success',
					showConfirmButton: true,
					confirmButtonColor: '#22c55e',
					confirmButtonText: 'Suivre ma commande'
				});
			} catch (error) {
				Swal.fire({
					icon: 'error',
					title: 'Aie !',
					text: "Une erreur s'est produite. Revenez plus tard.",
					showCancelButton: true,
					cancelButtonText: 'Retour à mon panier'
				});
				console.error('Erreur lors de la finalisation de la commande:', error);
			}
		} else {
			Swal.fire({
				icon: 'error',
				title: 'Aie !',
				text: 'Vous ne pouvez pas réaliser une commande de plus de 9 999,99€.',
				showCancelButton: false
			});
		}
	}
</script>

<main class="text-center overflow-hidden">
	{#if userInfo.role === 'ROLE_CUSTOMER'}
		<div>
			{#if error}
				<p class="error">{error.message} Please try again.</p>
			{/if}

			{#if clientSecret}
				<div class="mt-4">
					<p class="text-xl font-bold">Payez: {total.toFixed(2)} €</p>
				</div>
				<Elements
					{stripe}
					{clientSecret}
					theme="flat"
					labels="floating"
					variables={{ colorPrimary: '#7c4dff' }}
					rules={{ '.Input': { border: 'solid 1px #0002' } }}
					bind:elements
				>
					<form on:submit|preventDefault={submit}>
						<LinkAuthenticationElement />
						<PaymentElement />
						<Address mode="billing" />

						<button disabled={processing}>
							{#if processing}
								Processing...
							{:else}
								Pay
							{/if}
						</button>
					</form>
				</Elements>
			{:else}
				Loading...
			{/if}
		</div>
	{:else}
		<div>Vous n'avez pas accès à cette page!</div>
	{/if}
</main>

<style>
	.error {
		color: tomato;
		margin: 2rem 0 0;
	}

	form {
		display: flex;
		flex-direction: column;
		gap: 10px;
		margin: 2rem 0;
	}

	button {
		padding: 1rem;
		border-radius: 5px;
		border: solid 1px #ccc;
		color: black;
		background: var(--link-color);
		font-size: 1.2rem;
		margin: 1rem 0;
	}
</style>
