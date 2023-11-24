export const load = async (params) => {
	const idMenu = params.params.idMenu;

    const responseMenu = await fetch(`http://localhost:8080/api/menus/${idMenu}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const menu = await responseMenu.json();

    return {
        menu
    };
};
