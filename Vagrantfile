# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  
  # Cria uma máquina virtual usando a imagem do Ubuntu 64 bits
  config.vm.box = "ubuntu/trusty64"

  # Define uma máquina virtual chamada "web"

	config.vm.define :my_virtualbox do |web_config|

        # Permite acessar o endereço "localhost:8080" para acessar a porta 80 na

        "guest machine"

        config.vm.network "forwarded_port", guest: 80, host: 8080

    

        # Cria uma rede privada que permite apenas acesso do host à maquina

        # Utilizando um ip específico.

        web_config.vm.network "private_network", ip: "189.6.24.246"

  end	
	config.vm.provision :chef_solo do |chef|
	    chef.cookbooks_path = "cookbooks"
	    chef.add_recipe 'maven'
	    chef.add_recipe 'android-sdk'
            
		chef.json = {
		      'java' => {
			'jdk_version' => '7'
		      },
		      'android-sdk' => {
			'owner' => 'vagrant',
			'group' => 'vagrant',
			'components' => ['android-20', 'android-19'],
			# 'setup_root' => '/opt/somewhere',
			# 'with_symlink' => false,
			# 'java_from_system' => true,
			'maven_rescue' => true,
			'maven_local_repository' => '/home/vagrant/.m2/repository'
		      }
		}
	end
 end
