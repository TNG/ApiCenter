INSERT INTO SPECIFICATION_ENTITY VALUES ('b6b06513d2594fafb34ba216b3daad6a', 'remoteUrl', 'Description', 'Spec1');
INSERT INTO SPECIFICATION_ENTITY VALUES ('f67cb0a6c31b424bbfbbab0e163955ca', 'remoteUrl', 'Description', 'Spec2');
INSERT INTO SPECIFICATION_ENTITY VALUES ('af0502a2741040e490fd3504f67de1ee', 'remoteUrl', 'Description', 'Spec3');

INSERT INTO VERSION_ENTITY VALUES('v1', '{"info": {"title": "Spec1",  "version": "v1", "description": "Description"}}', '2018-01-01 11:00:00.000', 'b6b06513d2594fafb34ba216b3daad6a');
INSERT INTO VERSION_ENTITY VALUES('v1', '{"info": {"title": "Spec2",  "version": "v1", "description": "Description"}}', '2018-01-01 11:00:00.000', 'f67cb0a6c31b424bbfbbab0e163955ca');
INSERT INTO VERSION_ENTITY VALUES('v2', '{"info": {"title": "Spec2",  "version": "v2", "description": "Description"}}', '2018-01-01 10:00:00.000', 'f67cb0a6c31b424bbfbbab0e163955ca');
INSERT INTO VERSION_ENTITY VALUES('1.0', '{"info": {"title": "Spec3",  "version": "1.0", "description": "Description"}}', '2018-01-01 11:00:00.000', 'af0502a2741040e490fd3504f67de1ee');
INSERT INTO VERSION_ENTITY VALUES('1.1', '{"info": {"title": "Spec3",  "version": "1.1", "description": "Description"}}', '2018-01-01 10:00:00.000', 'af0502a2741040e490fd3504f67de1ee');