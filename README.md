## Question 기능 목록
- [x] 질문 생성
  - [x] title이 존재해야한다.
  - [x] contents가 존재해야한다.
  - [ ] responderId가 존재할 경우 해당 아이디를 가진 회원이 존재해야한다. (회원 기능과 함께 개발)
    
- [x] 질문 조회
  - [x] 전체 조회
  - [x] 단건 조회

- [x] 질문 수정
  - [x] 요청의 id와 일치하는 질문이 존재해야한다.
  - [x] title, contents, responderId는 기존 값과 같을 수 없다.
    
- [x] 질문 삭제
    - [x] 요청의 id와 일치하는 질문이 존재해야한다.

**POST /questions**

Authorization : 토큰

Request-Body
```json
{
  "title" : string
  "contents" : string
  "responderId" : integer || null
}
```

201 CREATED
Location : /questions/{id}

body
```json
{
  "id" : integer
  "title" : string
  "contents" : string
  "responderId" : integer || null 
  "createdAt" : string
  "lastModifiedAt" : string
}
```

**GET /questions**

200 OK

body
```json
[
  {
    "id" : integer
    "title" : string
    "contents" : string
    "responderId" : integer || null 
    "createdAt" : string
    "lastModifiedAt" : string
  },
  ...
]
```

**GET /questions/{id}**

200 OK

body

```json
{
  "id" : integer
  "title" : string
  "contents" : string
  "responderId" : integer || null 
  "createdAt" : string
  "lastModifiedAt" : string
}
```

**PUT /questions/{id}**

Authorization : 토큰

Request-Body

```json
{
  "title" : string
  "contents" : string
  "responderId" : integer || null 
}
```

200 OK

```json
{
  "id" : integer
  "title" : string
  "contents" : string
  "responderId" : integer || null
  "createdAt" : string
  "lastModifiedAt" : string
}
```

**DELETE /questions/{id}**

Authorization : 토큰

no body

204 NO-CONTENTS


## Answer 기능 목록
- [x] 답변 생성
    - [ ] contents가 존재해야 한다.
    - [ ] 답변을 달 해당 questionId가 존재해야 한다.

- [x] 답변 전체 조회
  - [ ] questionId가 존재해야 한다.

- [x] 답변 수정
  - [ ] questionId가 존재해야 한다.
  - [ ] 요청의 id와 일치하는 답변이 존재해야한다.
  - [ ] contents는 기존 값과 같을 수 없다.

- [x] 답변 삭제
  - [ ] questionId가 존재해야 한다.
  - [ ] 요청의 id와 일치하는 답변이 존재해야한다.

**POST /questions/{id}/answers**
Authorization : 토큰

Request-Body

```json
{
	"contents" : string
}
```

201 CREATED

LOCATION : **/questions/{id}/answers/{id}**

```json
{
	"id" : integer
	"questionId": integer
	"contents" : string
	"createdAt" : string
	"lastModifiedAt" : string
}
```

**GET /questions/{questionId}/answers**

200 OK

body

```json
[{
	"id" : integer
	"questionId": integer
	"contents" : string
	"createdAt" : string
	"lastModifiedAt" : string
},
{}...]
```

**PUT /questions/{questionId}/answers/{answerId}**

Authorization : 토큰

Request-Body

```json
{
  "contents": string
}
```

200 OK

```json
{
	"id" : integer
	"questionId": integer
	"contents" : string
	"createdAt" : string
	"lastModifiedAt" : string
}
```

**DELETE /questions/{questionId}/answers/{answerId}**

Authorization : 토큰

no body

204 NO-CONTENTS