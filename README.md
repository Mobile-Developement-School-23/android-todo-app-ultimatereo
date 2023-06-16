# Условия

## Теоретическая часть
Некоторые темы мы не будем рассматривать в рамках лекционного материала школ, чтобы успеть больше времени посвятить сложным и важным вещам. Поэтому часть тем, про которые говорим на лекциях поверхностно, нужно изучить самостоятельно.

В рамках этого домашнего задания это следующие темы:

- Activity Configuration Changes
- Intent & intent filters
- Activity Stack / launch mode - раз, два
- Fragments и AndroidX Navigation и про Single Activity
- Ресурсы в Android
- Про процессы
- Базовые компоненты Android-а, Service, BroadcastReceiver, раздел про ContentProvider, Инициализация библиотек с помощью ContentProvider

## Практическая часть
Мы начинаем делать приложение ToDo App, которое будем разрабатывать на протяжении всей лекционной части и которое поможет вам закрепить материал по всем пройденным темам.

В качестве примерного гайда для UI приложения можно использовать следующий дизайн в Figma.

### Требования к приложению
#### Общие требования

- Нужно создать новый проект для ToDo App
- Нужно реализовать базовые модели классов и UI согласно гайдам

#### Детальные требования

- реализовать дата класс Дела (TodoItem)
- реализовать класс-хранилище списка дел (TodoItemsRepository)
- реализовать UI ячейки Дела
- реализовать UI списка Дел
- реализовать кнопку добавления дела
- реализовать экран добавления/редактирования дела с обновлением данных

#### TodoItem

- класс должен быть kotlin data class
- id дела, обязательный параметр – String
- текст дела, обязательный параметр – String
- важность/значимость дела, обязательный параметр
  - должно быть 3 значения: “низкая”, “обычная”, “срочная»
- дедлайн выполнения, опциональный параметр
- флаг выполнения (сделана или нет), обязательный параметр
- дата создания, обязательный параметр
- дата изменения, опциональный параметр

#### TodoItemsRepository

- класс должен возвращать список дел
- метод добавления нового дела (дело передаётся как аргумент функции)
- в текущей реализации список дел сейчас можно захардкодить (минимум 10-20 значений)
  - дела должны быть максимально разнообразны, чтобы покрыть все комбинации возможных значений и проверить работу экрана наиболее полным образом

#### UI ячейки дела

- UI должен быть реализован на xml
- флаг готовности задачи
  - слева ячейки
  - кружочек красного/зелёного дела, либо другой визуальный элемент с двумя состояниями
- текст дела
  - текст занимает всё доступное пространство
  - максимум 3 строки, при превышении – многоточие в конце

#### UI списка дел

- должен быть реализован через RecyclerView
- в качестве ячеек должен выступать UI ячейки дела (из предыдущего пункта)
- список должен быть вертикальным и занимать всю ширину и высоту экрана
- передать в adapter список дел из TodoItemsRepository и отобразить


#### Кнопка добавления дела

- Floating Action Button
- Открывает экран добавления дела

#### UI добавления дела

- Отдельный экран
- Поле ввода, куда можно ввести описание дела
- Дедлайн (дату, которую можно выбрать в календаре, опционально)
- Приоритет дела (выпадающий список, по умолчанию стоит обычный приоритет)
- Кнопки закрытия / сохранения / удаления дела

#### Навигация

- Экран списка дел и добавления дела нужно реализовать на отдельных Fragment
- Клик в существующее дело в списке открывает экран редактирования дела (такой же, как добавление)
- Любая кнопка (сохранение/удаление/закрытие) на экране редактирования/добавления ведет к закрытию экрана и возвращению на список дел

### Дополнительное задание
- использовать DiffUtil для обновления списка/использование ListAdapter
- реализовать удаление дела с помощью функционала свайпа карточки
- по лонгклику на элемент из списка отрисовать анимацию, после которой появится меню с кнопками действий
- подписываться на изменения элементов в репозитории
  - может быть реализовано через callback или Flow
- можете также добавлять все, что придумает ваша фантазия и что будет полезно приложению. Если знаете, как сделать какие-то моменты лучше, чем описано здесь - делайте.

##### Общий балл оценки:
Критерии оценки. От 0 до 3 баллов за основное задание.
3 - Выполнил всё в рамках ТЗ без особых багов, код качественный
2 - Выполнил всё в рамках ТЗ, но есть баги/проблемы/плохой код
1 - Выполнил меньше или хуже, чем ожидалось по ТЗ
0 - Не сделал ничего или сделал ооооочень плохо, лучше бы вообще не делал
Дополнительно можно субъективно добавить 1 балл, если студент сделал больше, чем в ТЗ / проявил инициативу / сделал что-то интересное (есть за что похвалить).
