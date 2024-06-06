const neueAufgabeEingabe = document.getElementById('neue-aufgabe-eingabe');
const neueAufgabeHinzufügen = document.getElementById('neue-aufgabe-hinzufügen');
const ausstehendeAufgaben = document.getElementById('ausstehende-aufgaben');
const erledigteAufgaben = document.getElementById('erledigte-aufgaben');

neueAufgabeHinzufügen.addEventListener('click', () => {
  const neueAufgabe = neueAufgabeEingabe.value;
  if (neueAufgabe === '') {
    return;
  }

  const aufgabenElement = document.createElement('li');
  aufgabenElement.innerHTML = `
    <input type="checkbox" />
    <span>${neueAufgabe}</span>
    <button>Entfernen</button>`
    ;

  ausstehendeAufgaben.appendChild(aufgabenElement);

  neueAufgabeEingabe.value = '';

  aufgabenElement.querySelector('button').addEventListener('click', () => {
    aufgabenElement.remove();
  });

  aufgabenElement.querySelector('input').addEventListener('change', () => {
    if (aufgabenElement.querySelector('input').checked) {
      ausstehendeAufgaben.removeChild(aufgabenElement);
      erledigteAufgaben.style.display = 'block';
      erledigteAufgaben.appendChild(aufgabenElement);
    } else {
      erledigteAufgaben.removeChild(aufgabenElement);
      ausstehendeAufgaben.appendChild(aufgabenElement);
    }
  });
});
