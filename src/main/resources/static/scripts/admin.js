const $articlesDiv = document.querySelector(".articles");

async function getArticlesFromAdmin() {
    try {
        const response = await fetch('http://localhost:8080/admin');
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

getArticlesFromAdmin().then(data => {
    if (!data || !data.content) return; 
    data.content.forEach((article) => {
        const $currentArticleDiv = document.createElement("div");
        const $link = document.createElement('a');
        const $deleteButton = document.createElement('button');
        const $editButton = document.createElement('button');
        $link.href = "/article.html?id=" + article.id;        
        
        const $currentArticleTitle = document.createElement("h3");
        $currentArticleTitle.textContent = article.title;
        
        $link.appendChild($currentArticleTitle);
        
        $deleteButton.textContent = "Delete";
        $deleteButton.onclick = async () => {
            if(confirm("Are you sure you want to delete this article?")) {
                try {
                    const response = await fetch(`http://localhost:8080/delete/${article.id}`, {
                        method: 'DELETE'
                    });
                    if(response.ok) {
                        $currentArticleDiv.remove();
                    } else {
                        alert('Failed to delete article');
                    }
                } catch(error) {
                    console.error('Error deleting article:', error);
                    alert('Error deleting article');
                }
            }
        };

        $editButton.textContent = "Edit";
        $editButton.onclick = () => {
            window.location.href = "/edit.html?id=" + article.id;
        };
        
        $currentArticleDiv.appendChild($link);
        $currentArticleDiv.appendChild($editButton);
        $currentArticleDiv.appendChild($deleteButton);
        
        $articlesDiv.appendChild($currentArticleDiv);
    });
});

const $addButtton = document.querySelector(".addButton");
