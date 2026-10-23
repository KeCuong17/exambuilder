(() => {
  const list = document.querySelector('#question-list');
  const template = document.querySelector('#question-template');
  const addBtn = document.querySelector('#add-question');
  const maxQuestions = 30;

  function reindex() {
    const cards = [...list.querySelectorAll('.question-card')];
    cards.forEach((card, index) => {
      card.querySelector('.question-number').textContent = String(index + 1);
      card.querySelectorAll('[data-field]').forEach(field => {
        field.name = `questions[${index}].${field.dataset.field}`;
      });
      card.querySelector('.remove-question').disabled = cards.length === 1;
    });
    addBtn.disabled = cards.length >= maxQuestions;
  }

  addBtn.addEventListener('click', () => {
    if (list.querySelectorAll('.question-card').length >= maxQuestions) return;
    list.append(template.content.cloneNode(true));
    reindex();
    list.lastElementChild.querySelector('textarea').focus();
  });

  list.addEventListener('click', event => {
    if (!event.target.matches('.remove-question')) return;
    if (list.querySelectorAll('.question-card').length <= 1) return;
    event.target.closest('.question-card').remove();
    reindex();
  });

  reindex();
})();
