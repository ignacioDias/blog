const API_BASE_URL = 'http://localhost:8080';


function getArticleIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id');
}

async function loadArticle(articleId) {
    try {
        const response = await fetch(`${API_BASE_URL}/article/${articleId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const article = await response.json();
        
        document.getElementById('title').value = article.title;
        document.getElementById('content').value = article.content;
    } catch (error) {
        console.error('Error loading article:', error);
        alert('Failed to load article');
    }
}

// Load article on page load
const articleId = getArticleIdFromUrl();
if(articleId) {
    loadArticle(articleId);
} else {
    alert('No article ID provided');
    window.location.href = '/admin.html';
}

const $form = document.getElementById("articleForm");
$form.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/edit/${articleId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: title,
                content: content
            })
        });
        
        if (response.ok) {
            alert('Article updated successfully!');
            window.location.href = '/admin.html';
        } else {
            alert('Failed to update article');
        }
    } catch (error) {
        console.error('Error updating article:', error);
        alert('Error updating article');
    }
});