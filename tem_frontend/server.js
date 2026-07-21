const express = require('express');
const path = require('path');

const app = express();
const PORT = 3000;

const SPRING_BOOT_BASE = 'http://temp-backend-container:8081/api/temperatures';

// Serve static files (like index.html) from the project folder
app.use(express.static(__dirname));

// Accept incoming JSON payloads
app.use(express.json());

// Proxy Endpoint 1: Convert & Save (secured — needs X-API-KEY)
app.post('/api/convert', async (req, res) => {
    const { value, unit } = req.query;
    const apiKey = req.headers['x-api-key'];

    try {
        const response = await fetch(
            `${SPRING_BOOT_BASE}/convert?value=${value}&unit=${unit}`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-API-KEY': apiKey || ''
                }
            }
        );

        if (!response.ok) {
            if (response.status === 401) {
                return res.status(401).json({ error: 'Unauthorized: Invalid or missing API Key' });
            }
            return res.status(response.status).json({ error: 'Failed to communicate with Spring Boot' });
        }

        const data = await response.json();
        res.json(data);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Proxy Endpoint 2: Safety Check (open — no API key required by Spring Boot)
app.get('/api/safety-check', async (req, res) => {
    const { value, unit } = req.query;

    try {
        const response = await fetch(
            `${SPRING_BOOT_BASE}/safety-check?value=${value}&unit=${unit}`
        );

        if (!response.ok) {
            return res.status(response.status).json({ error: 'Failed to communicate with Spring Boot' });
        }

        const text = await response.text();
        res.json({ message: text });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Proxy Endpoint 3: View History (secured — needs X-API-KEY)
app.get('/api/history', async (req, res) => {
    const apiKey = req.headers['x-api-key'];

    try {
        const response = await fetch(`${SPRING_BOOT_BASE}/history`, {
            headers: {
                'X-API-KEY': apiKey || ''
            }
        });

        if (!response.ok) {
            if (response.status === 401) {
                return res.status(401).json({ error: 'Unauthorized: Invalid or missing API Key' });
            }
            return res.status(response.status).json({ error: 'Failed to communicate with Spring Boot' });
        }

        const data = await response.json();
        res.json(data);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

app.listen(PORT, () => {
    console.log(`Server running and accessible at: http://localhost:${PORT}`);
});