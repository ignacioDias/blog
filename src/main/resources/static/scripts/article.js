const API_BASE_URL = 'http://localhost:8080';

function getArticleIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id');
}

async function fetchArticle(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/article/${id}`);
        if (response.status === 404) {
            showError('Article not found');
            return null;
        }
        if (!response.ok) {
            throw new Error('Failed to fetch article');
        }
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
        showError('Error loading article: ' + error.message);
        return null;
    }
}

function displayArticle(article) {
    const $article = document.getElementById('article');
    
    $article.innerHTML = `
        <h1 class="article-title">${article.title}</h1>
        <div class="article-date">${article.createdAt || 'No date'}</div>
        <div class="article-content">${article.content || 'No content available'}</div>
    `;
    document.getElementById('loading').style.display = 'none';
    $article.style.display = 'block';
}

function showError(message) {
    const $error = document.getElementById('error');
    $error.textContent = message;
    $error.style.display = 'block';
    document.getElementById('loading').style.display = 'none';
}

// Load article on page load
const articleId = getArticleIdFromUrl();
if (articleId) {
    fetchArticle(articleId).then(article => {
        if (article) {
            displayArticle(article);
        }
    });
} else {
    showError('No article ID provided');
}