<script>
  import { onMount } from 'svelte';
  import { goto } from '$app/navigation';
  import Cookies from 'js-cookie';

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

  onMount(async () => {
        if (Cookies.get('token')) {
            await redirect()
        }
        else {
            goto('/auth')
        }
    })
</script>
