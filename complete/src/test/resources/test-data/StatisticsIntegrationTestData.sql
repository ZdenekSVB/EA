INSERT INTO public."user" (id, name, username) VALUES (1, 'Ivo', 'ivo');
INSERT INTO public."user" (id, name, username) VALUES (2, 'Marie', 'mar777');

INSERT INTO public.country (id, balance, name, owner_id) VALUES (1, 100.00, 'My country', 1);
INSERT INTO public.country (id, balance, name, owner_id) VALUES (2, 200.00, 'Savings for a car', 2);

INSERT INTO public."user_accounts" (users_id, accounts_id) VALUES (1, 1);
INSERT INTO public."user_accounts" (users_id, accounts_id) VALUES (2, 2);

INSERT INTO public.transaction (amount, source_account_id, target_account_id, id) VALUES (100.00, 1, 2, 'c4fd85db-55c5-4620-b7eb-73191a43520e');
INSERT INTO public.transaction (amount, source_account_id, target_account_id, id) VALUES (200.00, 1, 2, '736a5759-f681-40d7-92b5-0dff9327575e');
INSERT INTO public.transaction (amount, source_account_id, target_account_id, id) VALUES (50.00, 2, 1, '5fdba127-ab33-4881-bcf8-096e210fe7c9');
