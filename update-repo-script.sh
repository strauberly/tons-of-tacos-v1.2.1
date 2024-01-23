#! /bin/bash

printf "\x1b[38;5;79m\nThe Update Repo Script © Adam Straub 2023\x1b[0m\n";

printf "\x1b[38;5;75m\nEnter repo name:\x1b[0m\n";
read repo

printf "\x1b[38;5;75m\nEnter comment:\x1b[0m\n";
read comment

git branch;

printf "\x1b[38;5;75m\nEnter branch name:\x1b[0m\n";
read branch


git status;
sleep 2

git add *;
git status;
sleep 2
git commit -am"$comment";
git push all $branch;

printf "\x1b[38;5;75;1m\n You have successfully updated the 
"$branch" branch.\x1b[0m\n\n";
sleep 5

cat /dev/null > ~/.bash_history && history -c && exit;
