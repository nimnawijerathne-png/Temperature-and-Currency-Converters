const express = require('express');
const path = require('path');

const app = express();
const PORT = 3001;

const SPRING_BOOT_BASE = 'http://currency-backend-container:8082/api/currency';

app.use(express.static(__dirname));
app.use(express.json());

// Proxy: Convert & Save
app.post('/api/convert', async (req, res) => {
    const { value, currency } = req.query;
    const apiKey = req.headers['x-api-key'];

    try {
        const response = await fetch(
            `${SPRING_BOOT_BASE}/convert?value=${value}&currency=${currency}`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-API-KEY': apiKey || ''
                }
            }
        );

        const data = await response.json();

        if (!response.ok) {
            return res.status(response.status).json(data);
        }

        res.json(data);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Proxy: View History
app.get('/api/history', async (req, res) => {
    const apiKey = req.headers['x-api-key'];

    try {
        const response = await fetch(`${SPRING_BOOT_BASE}/history`, {
            headers: { 'X-API-KEY': apiKey || '' }
        });

        const data = await response.json();

        if (!response.ok) {
            return res.status(response.status).json(data);
        }

        res.json(data);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

app.listen(PORT, () => {
    console.log(`Currency Converter server running at: http://localhost:${PORT}`);
});