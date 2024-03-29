Тестовое задание «Backend разработчик»
Описание: Необходимо разработать API, на тему службы уведомлений.
Задание: В системе есть некоторые уведомления, модель которых описана как {
    id: 1,
    type: oneOf[SUCCESS, WARNING, FAIL],
    title: “some text“,
    content: “some text“,
    lastSentAt: timestamp
}
Необходимо реализовать: 
1) Возможность подключения к приложению через WebSocket
2) Возможность создания / получения / изменения / удаления уведомлений
3) Возможность моментально отправить уведомление подключенным клиентам
4) Возможность запланировать отправку уведомления на определенное время
5) REST Api: 
GET => `/api/notifications` => Получение всех уведомлений из бд
POST => `/api/notifications` => Создание уведомления
PUT => `/api/notifications/{id}` => Изменить уведомление по id
DELETE => `/api/notifications/{id}` => Удалить уведомление по id
POST => `/api/notifications/{id}/send` => Отправка уведомления

Требования к обмену данными между сервером и клиентом:
1) Обмен данными между сервером и клиентом осуществляется в формате JSON
2) Входящие пакеты уведомлений {
    id: 1,
    type: oneOf[SUCCESS, WARNING, FAIL],
    title: “some text“,
    content: “some text“,
    lastSentAt: timestamp
}
3) Приложение должно каждые 2 секунды обращаться к подключенным
клиентам и передавать пакет проверки соединения в формате JSON {
    id: UUID,
    message: «ping»
}
4) При отсутствии ответа со стороны клиента в течении 5 секунд, приложение
разрывает соединение(отключает клиента)
Ответ от клиента {
    id: UUID,
    message: «pong»
}
5) Приложение может быть реализовано с использованием одной из следующих
технологий * Java / Spring Boot / Quarkus * PHP / Laravel * Python / Ruby On Rails / Django * C / C++
6) Результат работы требуется загрузить на публичный git сервер.
7) Проект должен включать в себя исходный код, инструкцию по сборке
приложения, dump базы данных
8) Приложение должно использовать базу данных MySQL / PostgreSQL / MariaDB
