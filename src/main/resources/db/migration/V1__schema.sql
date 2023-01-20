DROP TABLE IF EXISTS allowance;
DROP TABLE IF EXISTS tasktype;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS tasktype_allowance;
DROP TABLE IF EXISTS task_allowance;

CREATE TABLE allowance (
    id INT NOT NULL AUTO_INCREMENT,
    balance DOUBLE,
    user_uuid VARCHAR(64),
    PRIMARY KEY(id)
);

CREATE TABLE tasktype (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    cadence VARCHAR(32),
    category VARCHAR(32),
    archived BOOLEAN NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE task (
    id INT NOT NULL AUTO_INCREMENT,
    date DATE NOT NULL,
    complete BOOLEAN NOT NULL,
    satisfactory BOOLEAN NOT NULL,
    tasktype_id INT NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT fk_tasktype_task_id FOREIGN KEY (tasktype_id) REFERENCES tasktype (id)
);

CREATE TABLE tasktype_allowance (
    id INT NOT NULL AUTO_INCREMENT,
    tasktype_id INT NOT NULL,
    allowance_id INT NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT fk_tasktype_allowance_id FOREIGN KEY (tasktype_id) REFERENCES tasktype (id),
    CONSTRAINT fk_allowance_tasktype_id FOREIGN KEY (allowance_id) REFERENCES allowance (id)
);

CREATE TABLE task_allowance (
    id INT NOT NULL AUTO_INCREMENT,
    task_id INT NOT NULL,
    allowance_id INT NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT fk_task_allowance_id FOREIGN KEY (task_id) REFERENCES task (id),
    CONSTRAINT fk_allowance_task_id FOREIGN KEY (allowance_id) REFERENCES allowance (id)
);