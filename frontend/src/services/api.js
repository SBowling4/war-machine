const API_BASE_URL = 'http://localhost:8080/api';

export async function getPlayers(season) {
    const response = await fetch(
        `${API_BASE_URL}/players?season=${season}`
    );

    if (!response.ok) {
        throw new Error('Failed to load players');
    }

    return response.json();
}

export async function calculateWar(playerId, season, metric) {
    const response = await fetch(
        `${API_BASE_URL}/war?playerId=${playerId}&season=${season}&metric=${metric}`
    );

    if (!response.ok) {
        throw new Error('Failed to calculate WAR');
    }

    return response.json();
}