export const load = async (loadEvent) => {
	const { fetch, cookies, params } = loadEvent;
	const token = cookies.get('token');
    
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const idMenu = params.idMenu;

    const responseMenu = await fetch(`http://localhost:8080/api/menus/${idMenu}`, {
        method: 'GET',
        headers: headersList
    });
    const menu = await responseMenu.json();

    const reponseAllCategories = await fetch('http://localhost:8080/api/categories/', {
		method: 'GET',
		headers: headersList
	});
	const allCategories = await reponseAllCategories.json();

    const responseUserInfo = await fetch('http://localhost:8080/auth/getUserInfo', {
		method: 'GET',
		headers: headersList
	});
	const userInfo = await responseUserInfo.json();

    return {
        menu,
        allCategories,
        userInfo,
								idMenu
    };
};
