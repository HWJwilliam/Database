CREATE TABLE Account
(
    Username VARCHAR PRIMARY KEY,
    Password VARCHAR NOT NULL
);

CREATE TABLE Stock_list
(
    ID VARCHAR PRIMARY KEY
);

CREATE TABLE Portfolio
(
    ID VARCHAR PRIMARY KEY,
    Cash REAL
);

CREATE TABLE Have_stock
(
    Symbol VARCHAR,
    Share INT,
    Stock_list_ID VARCHAR,
    PRIMARY KEY (Symbol, Stock_list_ID),
    FOREIGN KEY (Symbol) REFERENCES Stocks(Symbol),
    FOREIGN KEY (Stock_list_ID) REFERENCES Stock_list(ID)
);

CREATE TABLE Stocks
(
    Symbol VARCHAR PRIMARY KEY
);

CREATE TABLE Stock_hist_info
(
    Timestamp DATE,
    Symbol VARCHAR,
    Open REAL,
    Close REAL,
    High REAL,
    Low REAL,
    Volume INT,
    PRIMARY KEY(Timestamp, Symbol),
    FOREIGN KEY (Symbol) REFERENCES Stocks(Symbol)
);

CREATE TABLE Friend_request
(
    Request_username VARCHAR,
    Receive_username VARCHAR,
    Decision BOOLEAN,
    Time TIMESTAMP,
    PRIMARY KEY(Request_username, Receive_username)
);

CREATE TABLE Review
(
    Account_username VARCHAR,
    Stock_list_ID VARCHAR,
    Review_text VARCHAR,
    PRIMARY KEY(Account_username, Stock_list_ID)
);

CREATE TABLE Share
(
    Sharing_account_username VARCHAR,
    To_share_account_username VARCHAR,
    Stock_list_ID VARCHAR,
    PRIMARY KEY(Sharing_account_username, To_share_account_username, Stock_list_ID)
);

CREATE TABLE Creates
(
    Account_username VARCHAR,
    Stock_list_ID VARCHAR PRIMARY KEY
);

CREATE TABLE Have_portfolio
(
    Account_username VARCHAR,
    Portfolio_ID VARCHAR PRIMARY KEY
);

CREATE TABLE Have_stock_list
(
    Stock_list_ID VARCHAR UNIQUE,
    Portfolio_ID VARCHAR PRIMARY KEY
);

CREATE TABLE Bought
(
    Portfolio_ID VARCHAR,
    Stock_symbol VARCHAR,
    Share INT,
    PRIMARY KEY(Portfolio_ID, Stock_symbol)
);