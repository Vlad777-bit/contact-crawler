import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10,
    duration: '30s',
};

const BASE = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
    const payload = JSON.stringify({
        seedUrl: 'https://example.com',
        maxDepth: 2,
        maxPages: 50
    });

    const res = http.post(`${BASE}/crawl`, payload, {
        headers: { 'Content-Type': 'application/json' },
        timeout: '30s',
    });

    check(res, {
        'POST /crawl status is 201': (r) => r.status === 201,
    });

    // Дополнительно дернем results (даже если задача ещё выполняется)
    const res2 = http.get(`${BASE}/results?page=0&size=20`, { timeout: '30s' });

    check(res2, {
        'GET /results status is 200': (r) => r.status === 200,
    });

    sleep(1);
}
