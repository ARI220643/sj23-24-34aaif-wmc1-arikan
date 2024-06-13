function schuelerHinzufuegen() {
    const schuelerName = document.getElementById('schuelerName').value;
    if (schuelerName === '') {
        alert('Bitte geben Sie einen Namen ein');
        return;
    }

    const tabelleBody = document.querySelector('#notenTabelle tbody');
    const reihe = document.createElement('tr');
    reihe.className = 'schueler';
    reihe.innerHTML = `
        <td>${schuelerName}</td>
        <td><input type="number" class="noten-eingabe" min="1" max="5"></td>
        <td><input type="number" class="noten-eingabe" min="1" max="5"></td>
        <td><input type="number" class="noten-eingabe" min="1" max="5"></td>
        <td><input type="number" class="noten-eingabe" min="1" max="5"></td>
        <td class="durchschnittsnote"></td>
        <td><button onclick="notenEinreichen(this)">OK</button></td>
    `;

    tabelleBody.appendChild(reihe);
    document.getElementById('schuelerName').value = '';
}

function notenEinreichen(button) {
    const reihe = button.closest('tr');
    const notenEingaben = reihe.querySelectorAll('.noten-eingabe');
    let summe = 0;
    let anzahl = 0;

    notenEingaben.forEach((eingabe) => {
        const note = parseInt(eingabe.value);
        const zelle = eingabe.parentElement;

        if (!isNaN(note) && note >= 1 && note <= 5) {
            zelle.textContent = note;
            zelle.classList.remove('note-1', 'note-5');

            if (note === 1) {
                zelle.classList.add('note-1');
            } else if (note === 5) {
                zelle.classList.add('note-5');
            }

            summe += note;
            anzahl++;
        } else {
            alert('Bitte geben Sie eine gültige Note zwischen 1 und 5 ein.');
            return;
        }
    });

    if (anzahl > 0) {
        const durchschnittsnote = (summe / anzahl).toFixed(2);
        reihe.querySelector('.durchschnittsnote').textContent = durchschnittsnote;
    }
}

function pdfErstellen() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    doc.setFontSize(10);

    const tabelle = document.getElementById('notenTabelle');
    const reihen = tabelle.querySelectorAll('tr');
    let y = 10;

    reihen.forEach((reihe, reihenIndex) => {
        const zellen = reihe.querySelectorAll('th, td');
        zellen.forEach((zelle, zellenIndex) => {
            if (zellenIndex < zellen.length - 1) {
                doc.text(zelle.textContent, 10 + zellenIndex * 30, y);
            }
        });
        y += 10;
    });

    doc.save('noten.pdf');
}