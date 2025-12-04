const $articlesDiv = document.querySelector(".articles");

async function getArticles() {
    try {
        const response = await fetch('http://localhost:8080/home');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log(data);
        return data;
    } catch (error) {
        console.error('Error fetching articles:', error);
    }
}

getArticles().then(data => {
    if (!data) return; 
    data.forEach((article) => {
        const $currentArticleDiv = document.createElement("div");
        $currentArticleDiv.style.cursor = "pointer";
        $currentArticleDiv.onclick = () => {
            window.location.href = "/article.html?id=" + article.id;
        };
        
        const $currentArticleTitle = document.createElement("h3");
        $currentArticleTitle.textContent = article.title;
        
        const $currentArticleDate = document.createElement("p");
        $currentArticleDate.textContent = article.createdAt;
        
        $currentArticleDiv.appendChild($currentArticleTitle);
        $currentArticleDiv.appendChild($currentArticleDate);
        
        $articlesDiv.appendChild($currentArticleDiv);
    });
});
