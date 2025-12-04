const $form = document.getElementById("articleForm");
$form.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    
    try {
        const response = await fetch('http://localhost:8080/new', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: title,
                content: content
            })
        });
        
        if (response.ok) {
            alert('Article created successfully!');
            window.location.href = '/admin.html';
        } else {
            alert('Failed to create article');
        }
    } catch (error) {
        console.error('Error creating article:', error);
        alert('Error creating article');
    }
});