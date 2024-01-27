#!/usr/bin.sh

# Set the variables
set server_ip "35.243.255.80"
set ssh_key_path "C:\Users\calin\google-cloud-ssh"
set ssh_key_passphrase "catalin"

# Use expect to provide the passphrase
ssh -i $ssh_key_path -o "IdentitiesOnly=yes" -o "BatchMode=yes" -o "StrictHostKeyChecking=no" -o "UserKnownHostsFile=/dev/null" -o "PasswordAuthentication=no" -o "ConnectTimeout=10" -o "PreferredAuthentications=publickey" -o "NumberOfPasswordPrompts=0" -o "PubkeyAuthentication=yes" -o "ServerAliveInterval=60" -o "ServerAliveCountMax=30" -o "ControlMaster=no" -o "ControlPersist=no" -o "ControlPath=no" -o "LogLevel=ERROR" -o "UserKnownHostsFile=/dev/null" -o "VisualHostKey=no" calincatalin99@$server_ip

expect "Enter passphrase for key"
send "$ssh_key_passphrase\r"
expect eof
