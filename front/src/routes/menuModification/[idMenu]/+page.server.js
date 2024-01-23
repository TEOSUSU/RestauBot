export const load = async (loadEvent) => {
	const { fetch, cookies, params } = loadEvent;
	const token = cookies.get('token');
    
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const idMenu = params.idMenu;

	const reponseAllCategories = await fetch('http://localhost:8080/api/categories/', {
		method: 'GET',
		headers: headersList
	});
	const allCategories = await reponseAllCategories.json();

	const reponseAllTypes = await fetch('http://localhost:8080/api/types/', {
		method: 'GET',
		headers: headersList
	});
	const allTypes = await reponseAllTypes.json();

    const reponseAllDishes = await fetch('http://localhost:8080/api/dishes/', {
		method: 'GET',
		headers: headersList
	});
	const allDishes = await reponseAllDishes.json();

	const reponseMenuSelected = await fetch(`http://localhost:8080/api/menus/${idMenu}`, {
		method: 'GET',
		headers: headersList
	});
	const menuSelected = await reponseMenuSelected.json();

	const responseUserInfo = await fetch('http://localhost:8080/auth/getUserInfo', {
		method: 'GET',
		headers: headersList
	});
	const userInfo = await responseUserInfo.json();

	return {
		allCategories,
		allTypes,
        allDishes,
		menuSelected,
		userInfo
	};
};

