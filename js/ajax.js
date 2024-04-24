/* global fetch */
var flag = 0; 

function toggleNewTerm() {
    document.getElementById("termsList").innerHTML = "";
    var container = document.getElementById("latinCharacterLinksContainer");
    container.innerHTML = "";
    var newTermInsert = document.querySelector('.new_term_insert');
    document.getElementById("latinCharacterLinksContainer").innerHTML = "";
    if (newTermInsert.style.display === 'none' || newTermInsert.style.display === '') {
        newTermInsert.style.display = 'block';
    } 
}


function toggleEditTerm() {
    document.getElementById("termsList").innerHTML = "";
    var container = document.getElementById("latinCharacterLinksContainer");
    container.innerHTML = "";
    var newTermInsert = document.getElementsByClassName('new_term_insert')[0];
    document.getElementById("latinCharacterLinksContainer").innerHTML = "";
    newTermInsert.style.display = 'none';
    flag = 0;
    generateAlphabetLinks();
}

function generateAlphabetLinks() {
    var alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var container = document.getElementById("latinCharacterLinksContainer");
    
    var contentContainer = document.createElement("div");
    contentContainer.classList.add("content-container");
    
    // Create and append the title
    var title = document.createElement("div");
    title.innerHTML = "<strong>Glossary terms and definitions:</strong>";
    title.classList.add("title");
    contentContainer.appendChild(title);
    
    // Create and append the description
    var description = document.createElement("div");
    description.innerHTML = "This Glossary includes terminology compiled by DiSCS Lab members." +
    "When citing terms and definitions, we encourage you to cite the source publication for the authoritative terminology and to understand it in its proper context." +
    "<br>Many terms on this website have different definitions, from multiple publications." +
    "<br><small><a href='#' id='downloadLink'>Download Glossary</a></small>";

    description.classList.add("description");
    contentContainer.appendChild(description);
    
    container.appendChild(contentContainer);
    
    container.appendChild(document.createElement("br"));
    
    var alphabetContainer = document.createElement("div");
    
    for (var i = 0; i < alphabet.length; i++) {
        var char = alphabet.charAt(i);
        var link = document.createElement("a");
        link.href = "#"; 
        link.textContent = char;
        link.classList.add("latin-character-link"); 
        alphabetContainer.appendChild(link);
    }
    
    container.appendChild(alphabetContainer);
    
    function downloadGlossary() {
        var link = document.createElement("a");
        link.href = "glossary_file/SOC-Glossary.xlsx";
        link.download = "SOC-Glossary.xlsx"; 
        document.body.appendChild(link);

        link.click();
        document.body.removeChild(link);
    }

    document.getElementById("downloadLink").addEventListener("click", downloadGlossary);
}



document.addEventListener("DOMContentLoaded", function() {
    generateAlphabetLinks();
    var container = document.getElementById("latinCharacterLinksContainer");
    container.addEventListener("click", function(event) {
        if (event.target.matches(".latin-character-link")) {
            var char = event.target.textContent;
            showTerms(char);
        }
    });
});


function showTerms(char) {
    fetch(`http://localhost:8080/projectaki/showtermsalphabetically?character=${char}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            var termsList = document.getElementById("termsList");
            termsList.innerHTML = "";

            data.forEach(term => {
                var link = document.createElement("a");
                link.href = "#"; 
                link.textContent = term.term;
                link.addEventListener("click", function(event) {
                    termsList.innerHTML = "";
                    var termDetailsDiv = document.createElement("div");
                    termDetailsDiv.classList.add("term-details");
                    
                    // Create and style paragraphs for term details
                    var termPara = document.createElement("p");
                    termPara.innerHTML = "<strong>" + term.term + "</strong>";
                    termPara.classList.add("term-term");
                    termDetailsDiv.appendChild(termPara);

                    var definitionPara = document.createElement("p");
                    definitionPara.innerHTML = "<strong>Definition:</strong> " + term.definition;
                    definitionPara.classList.add("term-definition");
                    termDetailsDiv.appendChild(definitionPara);

                    var sourcePara = document.createElement("p");
                    sourcePara.innerHTML = "<strong>Source:</strong> " + term.source;
                    sourcePara.classList.add("term-source");
                    termDetailsDiv.appendChild(sourcePara);

                    var linkPara = document.createElement("p");
                    linkPara.innerHTML = "<strong>Link:</strong> " + term.link;
                    linkPara.classList.add("term-link");
                    termDetailsDiv.appendChild(linkPara);

                    var updatedAtPara = document.createElement("p");
                    updatedAtPara.innerHTML = "<span class='small-text'>Last updated at: " + term.updated_at + "</span>";
                    updatedAtPara.classList.add("term-updated-at");
                    termDetailsDiv.appendChild(updatedAtPara);

                    termsList.appendChild(termDetailsDiv);
                    if(!flag){
                        // Create container for buttons
                        var buttonContainer = document.createElement("div");
                        buttonContainer.classList.add("button-container");

                        // Provide options for editing or deleting the term
                        var editButton = document.createElement("button");
                        editButton.textContent = "Edit Term";
                        editButton.classList.add("edit-term-button");
                        editButton.addEventListener("click", function() {
                            // Handle edit term functionality
                            editTerm(term);
                        });
                        buttonContainer.appendChild(editButton);

                        var deleteButton = document.createElement("button");
                        deleteButton.textContent = "Delete Term";
                        deleteButton.classList.add("delete-term-button");
                        deleteButton.addEventListener("click", function() {
                            // Handle delete term functionality
                            deleteTerm(term);
                        });
                        buttonContainer.appendChild(deleteButton);

                        termsList.appendChild(buttonContainer);
                    }
                });
                // Append the term link to the terms list
                termsList.appendChild(link);
                termsList.appendChild(document.createElement("br"));
            });
        })
        .catch(error => {
            alert(error);
            console.error('Error:', error);
        });
}

function editTerm(term) {
    var termsList = document.getElementById("termsList");
    termsList.innerHTML = "";

    var termDetailsDiv = document.createElement("div");
    termDetailsDiv.classList.add("term-details");

    var termNameInput = createTextInput(term.term);
    var definitionInput = createTextInput(term.definition);
    var sourceInput = createTextInput(term.source);
    var linkInput = createTextInput(term.link);

    termDetailsDiv.appendChild(termNameInput);
    termDetailsDiv.appendChild(definitionInput);
    termDetailsDiv.appendChild(sourceInput);
    termDetailsDiv.appendChild(linkInput);

    var updateButton = document.createElement("button");
    updateButton.textContent = "Update";
    updateButton.classList.add("update-term-button");
    updateButton.addEventListener("click", function() {
        // Access values of the input fields directly
        var termName = termNameInput.value;
        var definition = definitionInput.value;
        var source = sourceInput.value;
        var link = linkInput.value;

        // Perform update operation using fetched values
        fetch('http://localhost:8080/projectaki/updateterm', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                original_term: term.term, 
                term: termName,
                definition: definition,
                source: source,
                link: link
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const responseData = data;
            toggleEditTerm();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });

    termDetailsDiv.appendChild(updateButton);
    termsList.appendChild(termDetailsDiv);
}

function createTextInput(defaultValue) {
    var input = document.createElement("input");
    input.type = "text";
    input.value = defaultValue;
    input.classList.add("term-input");
    input.classList.add("description-input"); 

    return input;
}





function deleteTerm(term) {
    fetch('http://localhost:8080/projectaki/deleteterm', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
       body: new URLSearchParams({
            term: term.term
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        var termsList = document.getElementById("termsList");
        termsList.innerHTML = term.term + " was successfully deleted";
    })
    .catch(error => {
        console.error('Error deleting term:', error);
    });
}


function addNewTerm() {
    event.preventDefault();
    var term = document.getElementById("term").value;
    var definition = document.getElementById("definition").value;
    var source = document.getElementById("source").value;
    var link = document.getElementById("link").value;

    fetch('http://localhost:8080/projectaki/insertnewterm', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            term: term,
            definition: definition,
            source: source,
            link: link
        })
    })
        .then(response => {
            if (!response.ok) {

                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const responseData = data;
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
