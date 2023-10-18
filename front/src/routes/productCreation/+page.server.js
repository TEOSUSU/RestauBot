export const load = async () => {

	const reponseAllCategories = await fetch('http://localhost:8080/api/categories/', {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});
	const allCategories = await reponseAllCategories.json();

	return {
		allCategories
	};
};

