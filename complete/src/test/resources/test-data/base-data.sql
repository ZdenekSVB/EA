INSERT INTO public."user" (id, name, username) VALUES (1, 'Ivo', 'ivo');
INSERT INTO public."user" (id, name, username) VALUES (2, 'Marie', 'mar777');

INSERT INTO public.country (id, balance, name, owner_id) VALUES (1, 100.00, 'My country', 1);
INSERT INTO public.country (id, balance, name, owner_id) VALUES (2, 200.00, 'Savings for a car', 2);

INSERT INTO public."user_accounts" (users_id, accounts_id) VALUES (1, 1);
INSERT INTO public."user_accounts" (users_id, accounts_id) VALUES (2, 2);
INSERT INTO public."user_accounts" (users_id, accounts_id) VALUES (1, 2);

INSERT INTO public.transaction (id, amount, source_account_id, target_account_id) VALUES ('fffd85db-55c5-4620-b7eb-73191a43533e', 50.00, 1, 2);
INSERT INTO public.transaction (id, amount, source_account_id, target_account_id) VALUES ('5fdba127-ab33-4881-bcf8-096e210fe7c9', 50.00, 2, 1);
