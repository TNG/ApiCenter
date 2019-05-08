INSERT INTO SERVICE_ENTITY VALUES ('b6b06513-d259-4faf-b34b-a216b3daad6a', 'Description', 'remoteUrl', 'Spec1');
INSERT INTO SERVICE_ENTITY VALUES ('f67cb0a6-c31b-424b-bfbb-ab0e163955ca', 'Description', 'remoteUrl', 'Spec2');
INSERT INTO SERVICE_ENTITY VALUES ('af0502a2-7410-40e4-90fd-3504f67de1ee', 'Description', 'remoteUrl', 'Spec3');
INSERT INTO SERVICE_ENTITY VALUES ('unique-identifier',                    'Description', 'remoteUrl', 'Spec4');

INSERT INTO SPECIFICATION_ENTITY VALUES('1.0.0', '{"info": {"title": "Spec1",  "version": "1.0.0", "description": "Description"}}' , '2018-01-01 11:00:00.000', 'Description', '', '0', 'Spec1', 'b6b06513-d259-4faf-b34b-a216b3daad6a');
INSERT INTO SPECIFICATION_ENTITY VALUES('1.0.0', '{"info": {"title": "Spec4",  "version": "1.0.0", "description": "Description"}}' , '2018-01-01 11:00:00.000', 'Description', '', '0', 'Spec4', 'unique-identifier');
INSERT INTO SPECIFICATION_ENTITY VALUES('1.0.0', '{"info": {"title": "Spec2",  "version": "1.0.0", "description": "Description"}}' , '2018-01-01 11:00:00.000', 'Description', '', '0', 'Spec2', 'f67cb0a6-c31b-424b-bfbb-ab0e163955ca');
INSERT INTO SPECIFICATION_ENTITY VALUES('2.0.0', '{"info": {"title": "Spec2",  "version": "2.0.0", "description": "Description"}}' , '2018-01-01 10:00:00.000', 'Description', '', '0', 'Spec2', 'f67cb0a6-c31b-424b-bfbb-ab0e163955ca');
INSERT INTO SPECIFICATION_ENTITY VALUES('1.0.0','{"info": {"title": "Spec3",  "version": "1.0", "description": "Description"}}', '2018-01-01 11:00:00.000', 'Description', '', '0', 'Spec3', 'af0502a2-7410-40e4-90fd-3504f67de1ee');
INSERT INTO SPECIFICATION_ENTITY VALUES('1.1.0','{"info": {"title": "Spec3",  "version": "1.1", "description": "Description"}}', '2018-01-01 10:00:00.000', 'Description', '', '0', 'Spec3', 'af0502a2-7410-40e4-90fd-3504f67de1ee');
