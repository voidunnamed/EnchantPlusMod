scoreboard players add @s rusty_kills 1
advancement revoke @s only enchantplus:internal/rusty_kill_count
execute if score @s rusty_kills matches 10.. run advancement grant @s only enchantplus:rusty_ten_kills